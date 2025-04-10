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
	
	
	
/*------------------------------------------------- IMAGES BY USER ---------------------------------------------------------------*/	
	
	
//	public Image getProfileImageOfCurrentUser(int userId) {
//		User currentUser = findUserById(userId);
//		Image profileImage = currentUser.getProfileImage();
//		return profileImage;
//	}
	
	
	
//	@Override
//	public Image getProfileImageByUserId(int userId){
//		log.info("Action: Attempting to get the profile image of user with id: {}", userId);
//		User user = findUserById(userId);
//		Image profileImage = user.getProfileImage();
//		return profileImage;
//	}
	
	
	
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
	public Image saveImage(String userEmail,ImageRequest imageRequest) {
		log.info("Action: saving Image: {}", imageRequest.getImageName());
		User user = userRepo.findByEmail(userEmail)
				.orElseThrow(()-> new UserNotFoundException("Unable to find the User with email: " + userEmail));
		Image image = new Image();
		image.setImageName(imageRequest.getImageName());
		image.setUser(user);
		log.info("Action: Image: {} saved successfully by User_Email: {}.", imageRequest.getImageName(), userEmail);
		imageRepo.save(image);
		return image;
	}
	

/*------------------------------------------------- IMAGE CRUD ---------------------------------------------------------------*/

	@Override
	public Image getImageById(int id) {
		log.info("Action: Getting image By Image_Id: {}", id);
		Image image = imageRepo.findById(id)
				.orElseThrow(() -> new ImageNotFoundException("Image with Id: " + id + " not found."));
		return image;
	}
	
	
	/*
	 * Deletes image by id only if it belongs to the  current-authenticated-user.
	 */
	@Override
	@Transactional
	public void deleteImageById(int imageId, int userId) {
		log.info("Action: Deleting Image of Id: {}", imageId);
		if(!imageRepo.existsByIdAndUserId(imageId, userId)) {
			 log.warn("Delete failed: Image {} not found or not owned by user {}", imageId, userId);
			throw new ImageNotFoundException("Image with Id: " + imageId + " not found or you do not have permission to delete.");	
		}
		 imageRepo.deleteByIdAndUserId(imageId, userId);
		 log.info("Action: Image of Id:{} deleted Successfully.", imageId);
	}
	

	
	@Override
	@Transactional
	public void deleteMultipleImagesById(List<Integer> idList) {
		log.info("Action: Deleting Multiple Images of Id's: {}", idList);		
		for(Integer id : idList) {
			Image image = imageRepo.findById(id)
					.orElseThrow(() -> new ImageNotFoundException("Image with Id: " + id + " not found."));
			log.info("Action: Image of Id:{} deleted Successfully.", id);
			imageRepo.delete(image);
		}
	}




//	@Override
//	public Image findByImageNameAndUserEmail(String imageName, String email) {
//		Image image = imageRepo.findByImageNameAndUserEmail(imageName, email)
//				.orElseThrow(() -> new ImageNotFoundException("Error: Image with name: " + imageName + " and user-email: " + email + "not found"));
//		return image;
//	}
//	
//	
//	@Override
//	public boolean doesImageExistsByImageNameAndUserEmail(String imageName, String email) {
//		if(imageRepo.existsByImageNameAndUserEmail(imageName, email)) {
//			return true;
//		}
//		return false;
//	}

	
	@Override
	public Image updateProfileImage(String imageName, int userId) {
		User user = findUserById(userId);
		Optional<Image> existingImage = imageRepo.findByImageNameAndUserId(imageName, userId);
		/*
		 * If the given imageName exists in DB it will return that
		 * */
		if(existingImage.isPresent()) {
			return existingImage.get();
		}
		/*
		 * If the given Image doesnot exists it will save a new Image for that user in the ImageRepository.
		 */
		Image newImage = new Image();
			newImage.setImageName(imageName);
			newImage.setUser(user);
		imageRepo.save(newImage);
		return newImage;
	}
	
	
	/* When new user is created it saves a default image for that user in imageRepository.
	 * OR 
	 * If the customProfile is removed we set the image back to default for that user.
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
			return existingDefaultImage.get();
		}
		/*
		 * If defaultImage is not present for the user create new DefaultImage.
		 */
		log.info("Saving Default profile picture for user : {}", user.getEmail());
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
	

	
	
	
	/*--------------Private method of user for images ----------------------------*/
	private User findUserById(int id) {
		return userRepo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User with id : " + id + " not found"));
	}
	
	
	
	
}//ends class.
