package com.jhaps.clientrecords.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.ImageDto;
import com.jhaps.clientrecords.entity.Image;

public interface ImageService {

	Page<ImageDto> getImagesOfActiveUser(Pageable pageable); /* Fetches all images of logged in User without userParam*/
	
	Page<ImageDto> getImagesByUser(int id, Pageable pageable); 	/* Fetches all images of selected User By id*/
	
	Page<ImageDto> getImagesByUser(String email, Pageable pageable);	/* Fetches all images of selected User By email*/
	
	
	ImageDto getImageById(int id); 
	
	void deleteImageById(int id);
	
	void saveImage(ImageDto imageDto);
	
	
	
	
}
