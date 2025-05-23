package com.jhaps.clientrecords.serviceImpl.system;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.request.user.UserImageUploadRequest;
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
import com.jhaps.clientrecords.service.CloudinaryService;
import com.jhaps.clientrecords.service.system.ImageService;
import com.jhaps.clientrecords.util.ImageFileManager;
import com.jhaps.clientrecords.util.ImageUploadPath;
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
	private ImageFileManager imageFileManager;
	private CloudinaryService cloudinaryService;
	
	
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
		MultipartFile imageFile = request.getImageFile();
		String originalImageName = request.getImageName();
		String contentType = imageFile.getContentType();
		String customImageName = imageFileManager.getCustomFileName(user);
		String folderName = String.valueOf(userId);
		Map<String, String> cloudinaryUploadDetails = cloudinaryService.uploadFile(imageFile, folderName, customImageName);
		String imageUrl = cloudinaryUploadDetails.get("url");
		String publicId = cloudinaryUploadDetails.get("publicId");
		Image image = Image.builder()
							.imageName(originalImageName)
							.storedFileName(customImageName)
							.contentType(contentType)
							.uploadTime(LocalDateTime.now())
							.url(imageUrl)
							.publicId(publicId)
							.user(user)
							.build();
		log.info("Action: Image: {} saved successfully by User_Email: {}.", request.getImageName(), user.getEmail());
		imageRepo.save(image);
		return image;
	}
	

	
	
	
	
	@Override
	public Image updateProfileImage(UserImageUploadRequest request, int userId) {
		if(request.getImageFile()==null) {
			log.warn("Image Multipart is null or not found in the UserImageUploadRequest.");
			throw new ImageException("Image Multipart not found in the request");
		}
		User user = findUserById(userId);
		MultipartFile imageFile = request.getImageFile();
		String contentType = request.getImageFile().getContentType();
		String folderName = String.valueOf(userId);
		/*
		 * Example : contentType = "image/jpeg", fileExtension will get = jpeg;
		 */
		String fileExtension = contentType.split("/")[1];
		String originalImageName = request.getImageName();
		String customImageName = imageFileManager.getCustomFileName(user); //generates a custom fileName to save in DB.
		Map<String, String> cloudinaryUploadDetails = cloudinaryService.uploadFile(imageFile, folderName, customImageName);
		String imageUrl = cloudinaryUploadDetails.get("url");
		String publicId = cloudinaryUploadDetails.get("publicId");
		if(imageUrl==null) {
			throw new ImageException("Unable to save the Image in the specified path. IO or Illegal State exception occured.");
		}
		/* Checking if the Image with "given name for current-user" Already exists in the Database*/
		boolean existsOnDb = imageRepo.existsByImageNameAndUser_Id(originalImageName, userId);
		
		if(existsOnDb) {
			Image imageOnDb = imageRepo.findByImageNameAndUser_Id(originalImageName, userId);
			return imageOnDb;
		}
		
		Image image = Image.builder()
							.imageName(originalImageName)
							.storedFileName(customImageName)
							.contentType(contentType)
							.uploadTime(LocalDateTime.now())
							.url(imageUrl)
							.publicId(publicId)
							.user(user)
							.build();
		
		imageRepo.save(image);
		log.info("Image {} with custom name {} saved successfully in the Image Repository for user {}.",
							originalImageName, customImageName, user.getEmail());
		return image;
	}//ends method
	
	
	
	
	
	
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
		User imageOwner = imageToDelete.getUser();
		if(imageOwner.getId() != currentUser.getId()) {
			log.warn("Not Authorized to delete image of id: {} by user: {}. Image doesn't belong to user{}",
					imageToDelete.getId(),currentUser.getId(), currentUser.getEmail());
			throw new ImageDeletionException("Not Authorized to delete the Image. You are not the owner of the image.");
		}
		
		/*
		 * Deleting the image from the FileDirectory.
		 * @Args: imageToDelete.getUrl() is the path of image inside RootDirectory.
		 */
//		imageFileManager.removeSingleImageFileFromStorage(imageToDelete.getUrl());
//		Getting PublicId of the Image this is needed to delete the image from the cloudinary.
		String publicId = imageToDelete.getPublicId();
		cloudinaryService.deleteSingleFile(publicId);
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
			
		
		try{
			/* Checking if the user has a profile Picture first.*/
			if(currentUser.getProfileImage().isPresent()) {
				int userProfileImageId = currentUser.getProfileImage().get().getId();
			
				for(Integer imageId : imageIdList) {
					/* Checking if the id in imageIdList contains the userProfileImage ---> imageId. */
						if(imageId.equals(userProfileImageId)) {
							log.info("Selected Image {} is also a userProfile Image.", imageId);
							currentUser.setProfileImage(null);
							log.info("Setting the user profileImage as null");
							userRepo.saveAndFlush(currentUser);
							log.info("Saving the userProfile After setting image to null.");
						}//ends-if
				}//ends-for
			
			}//ends-if
			
		   /*
			* Gets the ImagePublicIdPath from the ImageRepo using List<Integer> imageIds and userId. 
			*/
			List<String> imagePublicIdList = imageRepo.findPublicIdByImageIds(imageIdList, userId);
			/*
			 * removing the images from the directory first
			 */
//			imageFileManager.removeMultipleImageFileFromStorage(imageUrlList);
			cloudinaryService.deleteMultipleFiles(imagePublicIdList);
			
			/*
			 * Deleting the images from the imageRepo after removing from the Database.
			 */
			int totalDeleted = imageRepo.deleteByIdsAndUserId(imageIdList, userId);
			
			log.info("Action: Total {} Images, that of Id's:{} deleted Successfully.", totalDeleted, imageIdList);			
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
			imageFileManager.removeUserImageFolderFromStorage(userId);
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
