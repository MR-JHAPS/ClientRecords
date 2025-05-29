package com.jhaps.clientrecords.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

	
	Map<String, String> uploadFile(MultipartFile file, String folderName, String customFileName) ;
	
	String getSignedUrl(int userId, String storedFileName );
	
//	Map<String, String> uploadPdf(MultipartFile pdfFile, String folderName, String customFileName);
	
	void deleteMultipleFiles(List<String> publicIdList);
	
	void deleteSingleFile(String publicId);
	
	
}
