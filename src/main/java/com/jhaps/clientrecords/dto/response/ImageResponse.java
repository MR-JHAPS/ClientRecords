package com.jhaps.clientrecords.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {

	private int id;
	
	private String imageName;
	
	private String imageUrl;
	
	private LocalDateTime uploadedAt;
	
	private String contentType; // Eg: Image/jpeg
}
