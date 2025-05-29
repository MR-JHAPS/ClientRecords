package com.jhaps.clientrecords.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

	private int id;
	
	private String fileName;
	
	private String fileUrl;
	
	private LocalDateTime uploadedAt;
	
	private String contentType; // Eg: Image/jpeg
}
