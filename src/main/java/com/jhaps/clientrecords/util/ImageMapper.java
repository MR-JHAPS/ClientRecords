package com.jhaps.clientrecords.util;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.response.ImageDto;
import com.jhaps.clientrecords.entity.system.Image;

@Component
public class ImageMapper {

	
	
	/* ImageEntity to ImageDto*/
	public ImageDto toImageDto(Image image) {
		ImageDto dto = new ImageDto();
		dto.setImageName(image.getImageName());
		return dto;
	}
	
	
	/* ImageDto to ImageEntity */
	public Image toImageEntity(ImageDto imageDto) {
		Image entity = new Image();
		entity.setImageName(imageDto.getImageName());
		return entity;
	}
	
	
	
	
}
