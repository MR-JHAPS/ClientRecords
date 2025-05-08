package com.jhaps.clientrecords.serviceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jhaps.clientrecords.exception.system.ImageDeletionException;
import com.jhaps.clientrecords.exception.system.ImageException;
import com.jhaps.clientrecords.service.CloudinaryService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService{

	
	@Autowired
	private Cloudinary cloudinary;
	
	
	
	/* Return the Image-Url and Image-PublicId*/
	@Override
	public Map<String, String> uploadFile(MultipartFile file, String folderName, String customFileName) {
		/* Creates a custom path for each user with unique folderName. (FolderName will be the userId)*/
		String cloudinaryPath = folderName + "/" + customFileName;
		log.info("This is the value i put in the 'public_id': {} .",cloudinaryPath);
		try {
			Map<String, Object> uploadFile = cloudinary.uploader()
												.upload(file.getBytes(), ObjectUtils.asMap("public_id", cloudinaryPath) );
			/* "url" is the key of the Cloudinary-API it returns the url of the given file.*/
			String fileUrl = (String) uploadFile.get("url");
			Map<String, String> uploadDetails = new HashMap<>();
				uploadDetails.put("url", fileUrl);
				uploadDetails.put("publicId", cloudinaryPath);
			return uploadDetails;
		}catch (IOException e) {
			log.error("Unable to uploadFile to Cloudinary, Something went Wrong.");
			throw new ImageException("Failed To Upload The Image");
		}
	}


	
	
	@Override
	public void deleteMultipleFiles(List<String> publicIdList) {
		log.info("These are the public Id of the Image to be deleted : {}",publicIdList);
		publicIdList.forEach( 
						publicId ->{
							try {
								Map deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
								String response = (String) deleteResult.get("result");
								log.info("This is the response of deleteFile: {}",response);
								if(!response.equalsIgnoreCase("ok")) {
									throw new ImageDeletionException("Error! Unable to delete the Image From the cloudinary");
								}
								
							}catch (IOException e) {
								log.error("Unable to Delete the Image from the Cloudinary. Something went wrong.");
								 throw new ImageDeletionException("Failed to delete image");
							}//ends-catch
						}
					); //ends-forEach
	}//ends-method
	
	

	
	
	@Override
	public void deleteSingleFile(String publicId) {
		try{
			log.info("This is the public Id of the Image to be deleted : {}",publicId);
			Map deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
			String response = (String) deleteResult.get("result");
			log.info("This is the response of deleteFile: {}", response);
			if(!response.equalsIgnoreCase("ok")) {
				throw new ImageDeletionException("Error! Unable to delete the Image From the cloudinary");
			}
		}catch (IOException e) {
			log.error("Unable to Delete Single  Image from the Cloudinary. Something went wrong.");
			 throw new ImageDeletionException("Failed to delete Single image");
		}//ends-catch
	}//ends-method

	
	
	
	
	
}//ends class
