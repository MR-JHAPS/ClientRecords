package com.jhaps.clientrecords.service.system;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.response.ImageResponse;
import com.jhaps.clientrecords.entity.system.Image;

public interface ImageService {

/* ------------------------------------------- User-Image-CRUD --------------------------------------------*/
	
	Page<ImageResponse> getImagesByUserEmail(String email, Pageable pageable);	/* Fetches all images of selected User By email*/
	
	ImageResponse getSelfProfilePicture(String email); /* For user: to view their profile picture*/
	
	ImageResponse getProfileImageOfUserByUserId(int id); /* For admin: This is to get the profile picture of the user using user_Id. */
	
	ImageResponse saveImage(String email, ImageRequest request); /* Uploading/saving New Image */
	
	 /*	
	  *  When user deletes their account this method is also called from userService to delete all the images of that user.
	  *  For UserAccountDelete: Delete All Images of Selected User (i.e: By User email)
	  *  
	  *  @param email: email of the user whose images are to be deleted.
	  */
	void deleteImagesByUserEmail(String email);
	
	Image getDefaultProfileImage(); /* Gets the default image from the ImageRepository. */
	
	
/* ------------------------------------------- Image-CRUD --------------------------------------------*/
	
	ImageResponse getImageResponseById(int id); /* For user: Getting image by Image-Id*/
	
	Image getImageById(int id);
	
	void deleteImageById(int id); /* For user: Delete Image By image_Id*/
	
	void deleteMultipleImagesById(List<Integer> idList);
	
	
	
	
	
	
	
	
	
	
}
