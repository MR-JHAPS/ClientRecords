package com.jhaps.clientrecords.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequest {

	@NotBlank(message = "ImageRequest: imageName cannot be Empty")
	private String imageName;
	
	private MultipartFile imageFile;
	
	

}//ends dto
