package com.jhaps.clientrecords.util.mapper;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.request.FileRequest;
import com.jhaps.clientrecords.dto.response.FileResponse;
import com.jhaps.clientrecords.entity.system.UserFile;

@Component
public class FileMapper {

	
	public UserFile toFileEntity(FileRequest fileRequest) {
		UserFile entity = new UserFile();
		entity.setFileName(fileRequest.getFileName());
		return entity;		
	}
	
	
	public FileResponse toFileResponse(UserFile userFile, String fileUrl) {
		FileResponse response = new FileResponse();
		response.setId(userFile.getId());
		response.setFileName(userFile.getFileName());
		response.setFileUrl(fileUrl);
		response.setUploadedAt(userFile.getUploadTime());
		response.setContentType(userFile.getContentType());
		return response;
	}
	

	
	
	
	
}
