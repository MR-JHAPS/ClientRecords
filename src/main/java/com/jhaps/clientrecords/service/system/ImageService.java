package com.jhaps.clientrecords.service.system;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.response.ImageResponse;
import com.jhaps.clientrecords.entity.system.Image;

public interface ImageService {

/* ------------------------------------------- User-Image-CRUD --------------------------------------------*/	
	
	/* Get the profile-picture of the users using user_Id. */
//	Image getProfileImageByUserId(int id); 
	
	/*
	 * Get All images of current-user.
	 */
	Page<Image> getImagesOfCurrentUser(int userId, Pageable pageable);	
	
	/* 
	 * Get the Profile-Image of Current-Authenticated-User
	 */
//	Image getProfileImageOfCurrentUser(int userId); 
	
	
	
	/* Upload/save New Image */
	Image saveImage(String email, ImageRequest request);
	
	
	/*
	 * Main Usage : 1. When registering new user to set the default profile image.
	 * 				2. When removing userProfile Image it is set to default image.
	 *  Saves the default Image in image repository of that given user.
	 * @param usedId is the id of the user that we want to save a new default image of.
	 * */
	Image saveDefaultProfileImageForGivenUser(int userId);
/* ------------------------------------------- Image-CRUD --------------------------------------------*/
	
	/* 
	 * Get image by Image_id.
	 * This is selecting specific image from image gallery. 
	 */
	Image getImageById(int id); 
	
	
	
	/*
	 *  Checks for Existing image for that user
	 * If not found it will save new image in imageRepository.
	 */
	Image updateProfileImage(String imageName, int userId);
	
//	Image removeCustomProfileImageOfUser(int id);
	
	
	/* Delete single-image by image-id. */
	void deleteImageById(int imageId, int userId);
	
	/* Delete multiple-images by list of image-id's. */
	void deleteMultipleImagesById(List<Integer> idList);
	
	/* Deletes all images of given user. 
	 * @param userId is the given user. 
	 */
	void deleteAllImagesOfGivenUser(int userId);
	
	
	
	
//	/* Get the Image from repository using image-name and user-email.*/
//	Image findByImageNameAndUserEmail(String imageName, String email);
	
	
	
	
	
	
	
	
	
	
}
