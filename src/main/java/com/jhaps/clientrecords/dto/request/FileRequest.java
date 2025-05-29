package com.jhaps.clientrecords.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileRequest {

	@NotBlank(message = "FileRequest: fileName cannot be Empty")
	private String fileName;
	
	private MultipartFile file;
	
	

}//ends dto
