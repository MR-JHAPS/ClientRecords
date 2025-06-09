package com.jhaps.clientrecords.serviceImpl;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.jhaps.clientrecords.exception.system.FileDeletionException;
import com.jhaps.clientrecords.exception.system.FileException;
import com.jhaps.clientrecords.service.CloudinaryService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService{

	
	@Autowired
	private Cloudinary cloudinary;
	
	
	
	/** 
	 * Return the Image-Url and Image-PublicId to save in the Database
	 * PublicId : is like userFolder/fileName (eg: "41/defaultImage.jpg"). Used to delete specific picture of specific user.
	 */
	@Override
	public Map<String, String> uploadFile(MultipartFile file, String userId, String customFileName) {
		/* Creates a custom path for each user with unique folderName. (FolderName will be the userId)*/
//		String publicId = folderName + "/"+ UUID.randomUUID() + "_"  + customFileName;
		String publicId = userId+ "/" + customFileName;
		try {			
			Map<String, Object> uploadOptions =	ObjectUtils.asMap("public_id", publicId,
																	"type", "authenticated");
//			Map<String, Object> uploadOptions =	ObjectUtils.asMap("public_id", publicId);
//			uploading to cloudinary.
			Map<String , String> uploadResponse = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
			
			return Map.of(
					"publicId", (String) uploadResponse.get("public_id")
					);
				
		}catch (IOException e) {
			log.error("Unable to uploadFile to Cloudinary, Something went Wrong.");
			throw new FileException("Failed To Upload The Image");
		}
	}


	
	@Override
	public  String getSignedUrl(int userId, String storedFileName ){
		
		if(storedFileName==null || storedFileName.isEmpty()) {
			return null;
		}
		
		try {
			String folderName = String.valueOf(userId);
			String publicId = folderName + "/" + storedFileName;

		    String imageUrl = cloudinary.url()
	                .type("authenticated")
	                .publicId(publicId)
	                .secure(true)
	                .signed(true)
	                .generate();
		  return imageUrl;
	   } catch (Exception ex) {
	        log.error("Failed to generate signed URL for user {} file {}", userId, storedFileName, ex);
	        throw new FileException("Failed to generate secure resource URL");
	    }
	}//ends-method
	
	

	
	
	@Override
	public void deleteSingleFile(String publicId) {
		try{
			log.info("This is the public Id of the Image to be deleted : {}",publicId);
			Map<String, String> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("type","authenticated"));
			String response = (String) deleteResult.get("result");
			log.info("This is the response of deleteFile: {}", response);
			if(!response.equalsIgnoreCase("ok")) {
				throw new FileDeletionException("Error! Unable to delete the Image From the cloudinary");
			}
		}catch (IOException e) {
			log.error("Unable to Delete Single  Image from the Cloudinary. Something went wrong.");
			 throw new FileDeletionException("Failed to delete Single image");
		}//ends-catch
	}//ends-method


	
	
	
	@Override
	public void deleteMultipleFiles(List<String> publicIdList) {
		log.info("These are the public Id of the Image to be deleted : {}",publicIdList);
		publicIdList.forEach( 
						publicId ->{
							try {
								Map<String, String> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("type","authenticated"));
								String response = (String) deleteResult.get("result");
								log.info("This is the response of deleteFile: {}",response);
								if(!response.equalsIgnoreCase("ok")) {
									throw new FileDeletionException("Error! Unable to delete the Image From the cloudinary");
								}
								
							}catch (IOException e) {
								log.error("Unable to Delete the Image from the Cloudinary. Something went wrong.");
								 throw new FileDeletionException("Failed to delete image");
							}//ends-catch
						}
					); //ends-forEach
	}//ends-method
	
	

	
	
}//ends class
