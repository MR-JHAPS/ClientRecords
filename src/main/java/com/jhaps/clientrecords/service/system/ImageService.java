package com.jhaps.clientrecords.service.system;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.request.user.UserImageUploadRequest;
import com.jhaps.clientrecords.dto.response.ImageResponse;
import com.jhaps.clientrecords.entity.system.Image;

public interface ImageService {

/* ------------------------------------------- User-Image-CRUD --------------------------------------------*/	
	
	/*
	 * Get All images of current-user.
	 */
	Page<Image> getImagesOfCurrentUser(int userId, Pageable pageable);	
	
	
	/* 
	 * Upload/save New Image 
	 */
	Image saveImage(int userId , ImageRequest request);
	
	
	/*
	 * Main Usage : 1. When registering new user to set the default profile image.
	 * 				2. When removing userProfile Image it is set to default image.
	 *  Saves the default Image in image repository of that given user.
	 * @param usedId is the id of the user that we want to save a new default image of.
	 * */
//	Image saveDefaultProfileImageForGivenUser(int userId);
/* ------------------------------------------- Image-CRUD --------------------------------------------*/
	
	/* 
	 * Get image by Image_id.
	 * This is selecting specific image from image gallery. 
	 */
	Image getImageById(int imageId, int userId); 
	
	
	
	/*
	 *  Checks for Existing image for that user
	 * If not found it will save new image in imageRepository.
	 */
//	Image updateProfileImage(String imageName, int userId);
	Image updateProfileImage(UserImageUploadRequest request, int userId);
	
//	Image removeCustomProfileImageOfUser(int id);
	
	
	/* Delete single-image of current-user by image-id. */
	void deleteImageById(int imageId, int userId);
	
	/* Delete multiple-images of current-user by list of image-id's. */
	void deleteMultipleImagesById(List<Integer> imageIdList, int userId);
	
	/* Deletes all images of given user. 
	 * @param userId is the given user. 
	 */
	void deleteAllImagesOfGivenUser(int userId);
	

	
	
	
	
	
}//ends interface.
