package com.jhaps.clientrecords.service.system;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.response.ImageDto;
import com.jhaps.clientrecords.entity.system.Image;

public interface ImageService {

	Page<ImageDto> getImagesByEmail(String activeUserEmail, Pageable pageable); /* Fetches all images of logged in User without userParam*/
	
//	Page<ImageDto> getImagesByUser(int id, Pageable pageable); 	/* Fetches all images of selected User By id*/
	
	Page<ImageDto> getImagesByUserEmail(String email, Pageable pageable);	/* Fetches all images of selected User By email*/
	
	
	ImageDto getImageById(int id); /* getting image by Image-Id*/
	
//	void deleteImage
	
	void deleteImageById(int id);
	
	void deleteImagesByUserEmail(String email);
	
	void saveImage(String email, ImageDto imageDto);
	
	
	
	
}
