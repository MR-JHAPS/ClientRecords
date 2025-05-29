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
	public Map<String, String> uploadFile(MultipartFile file, String folderName, String customFileName) {
		/* Creates a custom path for each user with unique folderName. (FolderName will be the userId)*/
		String cloudinaryPath = folderName + "/" + customFileName;
		try {			
			Map<String, Object> uploadOptions =	ObjectUtils.asMap("public_id", cloudinaryPath,
																	"type", "authenticated");
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
		
		try {
			String folderName = "user_" + String.valueOf(userId);
			String publicId = folderName + "/"+ UUID.randomUUID() + "_" + storedFileName;
//		    long expiresAt = Instant.now().plusSeconds(3600).getEpochSecond(); // 1 hour expiry
		    
//		    "e_" is expirationTime in cloudinary and "%d" is the unix time system for timestamp.
//		    Transformation transformation = new Transformation()
//		            							.rawTransformation(String.format("e_%d", expiresAt));  
		    return cloudinary.url()
	                .type("authenticated")
	                .publicId(publicId)
	                .secure(true)
	                .signed(true)
//	                .transformation(transformation)
	                .generate();
	   } catch (Exception ex) {
	        log.error("Failed to generate signed URL for user {} file {}", userId, storedFileName, ex);
	        throw new FileException("Failed to generate secure resource URL");
	    }
	}//ends-method
	
	

	
	
	@Override
	public void deleteSingleFile(String publicId) {
		try{
			log.info("This is the public Id of the Image to be deleted : {}",publicId);
			Map<String, String> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
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
								Map<String, String> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
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
	
	
	
//	-------------------------------------Probably not necessary-------------------------------------------------------------



//	@Override
//	public Map<String, String> uploadPdf(MultipartFile pdfFile, String folderName, String customFileName) {
//		log.info("Initiating the upload of Pdf file : {} to the cloudinary", customFileName);
//		String cloudinaryPath = folderName + "/" + customFileName;
//		try {
//			//  ObjectUtils.asMap("public_id", cloudinaryPath) telling cloudinary to create a custom path instead of generating random.			
//			Map<String, Object> uploadedFile = cloudinary.uploader().upload(pdfFile.getBytes(), ObjectUtils.asMap("public_id", cloudinaryPath));
//			// Getting the url from metaData response from cloudinary.			
//			String pdfUrl = (String) uploadedFile.get("url");
//			Map<String, String> uploadResponseDetails = new HashMap<>();
//			uploadResponseDetails.put("url", pdfUrl);
//			uploadResponseDetails.put("publicId", cloudinaryPath);
//			return uploadResponseDetails;
//		} catch (Exception e) {
//			log.error("Unable to upload PDF_File to Cloudinary, Something went Wrong.");
//			throw new PdfException("Failed To Upload The PDF_File");
//		}
//	}
//	
	
	
	
	
	
}//ends class
