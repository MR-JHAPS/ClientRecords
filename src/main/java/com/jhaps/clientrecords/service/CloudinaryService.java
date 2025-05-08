package com.jhaps.clientrecords.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

	
	String uploadFile(MultipartFile file, String folderName, String customFileName) throws IOException;
	
	
	
	
}
