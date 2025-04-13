package com.jhaps.clientrecords.serviceImpl.system;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.response.ImageResponse;
import com.jhaps.clientrecords.entity.system.Image;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.exception.system.DuplicateDataException;
import com.jhaps.clientrecords.exception.system.ImageDeletionException;
import com.jhaps.clientrecords.exception.system.ImageException;
import com.jhaps.clientrecords.exception.system.ImageNotFoundException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.system.ImageRepository;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.security.model.CustomUserDetails;
import com.jhaps.clientrecords.service.system.ImageService;
import com.jhaps.clientrecords.util.mapper.ImageMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ImageServiceImpl implements ImageService{

	private ImageRepository imageRepo;
	private UserRepository userRepo;
	
	
/*--------------------------------------------------- Private method of user for images ------------------------------------------------------------*/
	
	private User findUserById(int id) {
		return userRepo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User with id : " + id + " not found"));
	}
	
/*--------------------------------------------------------------------------------------------------------------------------------------------------*/	
	
	
	@Override
	public Image getImageById(int imageId, int userId) {
		log.info("Action: Getting image By Image_Id: {}", imageId);
		Image image = imageRepo.findByIdAndUserId(imageId, userId)
				.orElseThrow (() -> {
					log.error("Image with imageID {} and userId {} not found in the database.", imageId, userId);
					throw new ImageNotFoundException("Image with Image-Id: " + imageId + " and userId "+ userId +" not found.");
				});
		log.info("Image with imageID {} and userID {} fetched Successfully", imageId, userId);
		return image;
	}
	
	
	@Override
	public Page<Image> getImagesOfCurrentUser(int userId, Pageable pageable){
		log.info("Getting images for User of ID : ", userId);
		Page<Image> images = imageRepo.findByUser_Id(userId , pageable);
		if(images.isEmpty()) {
			log.info("Message: Images for User_ID : {} not found in Database.", userId);
		}else {
			log.info("Message: Fetched {} Images for User_ID : {} fetched.", images.getNumberOfElements(), userId);	
		}
		return images;
	}
	

	
	@Override
	@Transactional
	public Image saveImage(int userId,ImageRequest request) {
		log.info("Action: Saving Image: {}", request.getImageName());
		User user = findUserById(userId);
		/* 
		 * Checking if the image with the name exists for this user in the imageRepository.
		 */
		Optional<Image> imageOnDb = imageRepo.findByImageNameAndUserId(request.getImageName(), userId);
		if(imageOnDb.isPresent()) {
			log.warn("Image with name: {} of user : {}, already exists in the database.", request.getImageName(), user.getEmail());
			throw new DuplicateDataException("Image With name " + request.getImageName()
							+ " for user "+ user.getEmail()+" already exists in the database.");
		}
		/* If image does not exists in the database then save that image. */
		Image image = new Image();
		image.setImageName(request.getImageName());
		image.setUser(user);
		log.info("Action: Image: {} saved successfully by User_Email: {}.", request.getImageName(), user.getEmail());
		imageRepo.save(image);
		return image;
	}
	
	
	
	
	/* @Method: 
	 * 		When new user is created it saves a default-image for that user in imageRepository.
	 * 		OR 
	 * 		If the customProfileImage is removed we set the image back to defaultImage for that user.
	 */
	@Override
	public Image saveDefaultProfileImageForGivenUser(int userId){
		String defaultProfileImage = "defaultImage.png";
		User user = findUserById(userId);		
		/*
		 * Checking if the default image is already present for this user in Image-Repository.
		 */
		Optional<Image> existingDefaultImage = imageRepo.findByImageNameAndUserId(defaultProfileImage, userId);
		if(existingDefaultImage.isPresent()) {
			log.info("Existing default image {} is found for user: {}", existingDefaultImage.get(), user.getEmail());
			return existingDefaultImage.get();
		}
		/*
		 * If defaultImage is not present for the user create new DefaultImage.
		 */
		log.info("Existing Default Image is not found So, Saving new Default profile picture for user : {}", user.getEmail());
		Image defaultImage = new Image();
		defaultImage.setImageName(defaultProfileImage);
		defaultImage.setUser(user);
		//saving new default image.
		Image savedImage = imageRepo.save(defaultImage);
		//if image saved return savedImage.
		if(savedImage!=null && savedImage.getId()!=0) {
			log.info("Successfully saved Default profile picture for user : {}", user.getEmail());
			return savedImage;
		}
		log.info("Error: Unable to save Default profile picture for user : {}", user.getEmail());
		throw new ImageException("ERROR: Unable to save Default Profile Image for the user: " + user.getEmail());	
	}


	@Override
	public Image updateProfileImage(String imageName, int userId) {
		User user = findUserById(userId);
		Optional<Image> existingImage = imageRepo.findByImageNameAndUserId(imageName, userId);
		/*
		 * If the given imageName exists in DB it will return that
		 * */
		if(existingImage.isPresent()) {
			log.info("Image with name {} for user {} already exists in the Database. Setting that image to user profile", imageName, user.getEmail());
			return existingImage.get();
		}
		/*
		 * If the given Image doesnot exists it will save a new Image for that user in the ImageRepository.
		 */
		log.info("Image with name {} not present in Database so saving a new image in the Image Repository", imageName);
		Image newImage = new Image();
			newImage.setImageName(imageName);
			newImage.setUser(user);
		imageRepo.save(newImage);
		log.info("Image {} saved successfully in the Image Repository for user {}.", imageName, user.getEmail());
		return newImage;
	}
	
	
	
	/*
	 * Deletes image by id only if it belongs to the  current-authenticated-user.
	 * And if the profile image is selected to delete it sets the current user profile
	 * to DefaultImage("defaultImage.png") and then deletes it to prevent Foreign Key Constraints issues.
	 */
	@Override
	@Transactional
	public void deleteImageById(int imageId, int userId) {
		log.info("Action: Deleting Image of Id: {}", imageId);
		if(!imageRepo.existsByIdAndUserId(imageId, userId)) {
			 log.warn("Delete failed: Image {} not found or not owned by user {}", imageId, userId);
			throw new ImageNotFoundException("Image with Id: " + imageId + " not found or you do not have permission to delete.");	
		}
		/* If image id selected to delete is userProfile than we need to set the default profile picture before deleting the image.*/
		Image imageToDelete = findImageById(imageId);
		User currentUser = findUserById(userId);
		Image userProfileImage = currentUser.getProfileImage();
		/*If userToDelete is the current Profile Image then firstly: Set the default-profile-image of that user and then delete the image.*/
		if(userProfileImage.equals(imageToDelete)) {
			Image defaultProfileImage = saveDefaultProfileImageForGivenUser(userId);
			log.info("Action: Setting the Default Image as User Profile Image because it was selected to delete.");
			currentUser.setProfileImage(defaultProfileImage); 
		}
		 imageRepo.deleteByIdAndUserId(imageId, userId);
		 log.info("Action: Image of Id:{} deleted Successfully.", imageId);
	}
	

	
	/*
	 * Deletes Multiple images by id only if it belongs to the current-authenticated-user.
	 */
	@Override
	@Transactional
	public void deleteMultipleImagesById(List<Integer> imageIdList, int userId) {
		log.info("Action: Deleting Multiple Images of Id's: {}", imageIdList);	
		User currentUser = findUserById(userId);
		int userProfileImageId = currentUser.getProfileImage().getId();
		try{
			for(Integer imageId : imageIdList) {
				/* Checking if the id in imageIdList contains the userProfileImage ---> imageId. */
					if(imageId.equals(userProfileImageId)) {
						currentUser.setProfileImage(null);
						userRepo.save(currentUser);
					}
					imageRepo.deleteByIdAndUserId(imageId, userId);
			}//ends-for
			log.info("Action: Images, that of Id's:{} deleted Successfully.", imageIdList);
			
			/*After deleting the images. If currentUser.getProfilePicture is null(i.e: deleted ),
			 *  Set the default profile for the image.*/
			if(currentUser.getProfileImage()==null) {
				Image defaultProfileImage = saveDefaultProfileImageForGivenUser(currentUser.getId());
				log.info("Action: Setting the Default Image as User Profile Image before deleting,"
										+ " because the profile image was also selected to delete.");
				currentUser.setProfileImage(defaultProfileImage); 
				userRepo.save(currentUser);
			}
		}catch (Exception e) {
			log.error("Unable to Delete Multiple images with id's {}, of userId {}", imageIdList, userId);
			throw new ImageDeletionException("Error occured. Unable to delete Mulptiple Images of Id's : " + imageIdList + " of userId : "+ userId);
		}
	}

	
	
	/* Deletes all the images of given user from the imageRepository. */
	@Override
	@Transactional
	public void deleteAllImagesOfGivenUser(int userId) {		
		try{
			imageRepo.deleteAllImagesByUserId(userId);
			log.info("Delete all images with userId successful.");
		}catch (Exception e) {
			log.error("Error deleting images for user {}", userId, e);
			throw new DataIntegrityViolationException("Delete All images by user id failed.");
		}
	}

	

	
	
	
	/* Private Method to find image By id: */
	
	private Image findImageById(int imageId) {
		return imageRepo.findById(imageId)
					.orElseThrow(()-> new ImageNotFoundException("Error: Image with ID:" + imageId + " not found . "));
	}
	
	
	
	
}//ends class.
