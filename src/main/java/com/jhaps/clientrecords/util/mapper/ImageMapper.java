package com.jhaps.clientrecords.util.mapper;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.request.ImageRequest;
import com.jhaps.clientrecords.dto.response.ImageResponse;
import com.jhaps.clientrecords.entity.system.Image;

@Component
public class ImageMapper {

	
	public Image toImageEntity(ImageRequest imageRequest) {
		Image entity = new Image();
		entity.setImageName(imageRequest.getImageName());
		return entity;		
	}
	
	
	public ImageResponse toImageResponse(Image image) {
		ImageResponse response = new ImageResponse();
		response.setId(image.getId());
		response.setImageName(image.getImageName());
		response.setImageUrl("/images/"+image.getUrl());
		response.setUploadedAt(image.getUploadTime());
		response.setContentType(image.getContentType());
		return response;
	}
	

	
	
	
	
}
