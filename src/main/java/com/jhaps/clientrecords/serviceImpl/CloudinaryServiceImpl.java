package com.jhaps.clientrecords.serviceImpl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jhaps.clientrecords.service.CloudinaryService;


@Service
public class CloudinaryServiceImpl implements CloudinaryService{

	
	@Autowired
	private Cloudinary cloudinary;
	
	
	
	@Override
	public String uploadFile(MultipartFile file, String folderName, String customFileName) throws IOException {
		/* Creates a custom path for each user with unique folderName. (FolderName will be the userId)*/
		String cloudinaryPath = folderName + "/" + customFileName;
		Map<String, Object> uploadFile = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", cloudinaryPath));
		/* "url" is the key of the Cloudinary-API it returns the url of the given file.*/
		return (String) uploadFile.get("url");
	}

	
	
	
	
	
	
}
