package com.jhaps.clientrecords.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PdfUploadRequest {

	private MultipartFile pdfFile;
	
	private String fileName;
	
}
