package com.jhaps.clientrecords.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.jhaps.clientrecords.entity.system.User;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/*
 * This class manages the deletion and insertion of Image files / folder 
 * in the designated PATH.
 * 
 * ImageUploadPath.PATH.getPath() ----> this contains the root directory of where the image file will be.
 */


@Component
@Slf4j
public class ImageFileManager {

	
	/* Generates the Custom file name using given @Param. */
	@Transactional
	public String getCustomFileName(User user) {
		String rawEmailPrefix = user.getEmail().split("@")[0];
		String sanitizedEmailPrefix = rawEmailPrefix
									    .replaceAll("[^a-zA-Z0-9]", "_")  // Only allow alphanumeric + underscore
									    .substring(0, Math.min(rawEmailPrefix.length(), 25));  // Limit length
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));
		String customFileName = sanitizedEmailPrefix + timestamp ;
		return customFileName;
	}	
	
	
	
	
	/* Returns the filePath (userFolder/fileName.jpg) or null */
	@Transactional
	public String manageUploadPath(MultipartFile file, String customFileName, int folderName) {
		/* FolderName is the userId. */
		String folderNameString = String.valueOf(folderName) ;
		Path rootPath = Paths.get(ImageUploadPath.PATH.getPath());
		try {					
				Path userFolder = rootPath.resolve(folderNameString); //adding folderPath to root-path.
				if(!Files.exists(userFolder)) {
					log.info("Folder with name : {} currently does not exist. Creating the path {}.", folderName, userFolder);
					Files.createDirectories(userFolder); //creating a folder if does not exists
				}
				Path destinationPath = userFolder.resolve(customFileName); //joins customFileName inside userFolderDirectory.
					
				InputStream inputStream = file.getInputStream();
				Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
				return (folderName + "/" + customFileName); //this excludes the rootPath.
				
			} catch (IllegalStateException e) {
				log.error("Illegal state exception. Cannot save the file in {}/{}/", rootPath, folderName);
				return null;
			} catch (IOException e) {
				log.error("IO-exception. Cannot save the file in {}/{}/", rootPath, folderName);
				return null;
			}
	}//ends method

	
	/* For removal of single File */
	@Transactional
	public void removeSingleImageFileFromStorage(String imagePath) {
		/*user will send the imagePath(url) from ImageDatabase.
		 extracting ImageName form (imagePath) "userFolder/image.jpg" */
		String imageName = imagePath.split("/")[1];
//		Path completeImagePath = Paths.get(ImageUploadPath.PATH.getPath()+imagePath);
		Path rootPath = Paths.get(ImageUploadPath.PATH.getPath());
		Path completeImagePath = rootPath.resolve(imagePath);
		try {
			if(Files.deleteIfExists(completeImagePath)) {
				log.info("Image File {} deleted from the Directory ", imageName);
			}	
		}catch (Exception e) {
			log.error("Error deleting the Image File {} from the upload Folder.", imageName);
		}
	}
	
	
	
	/* Removes Multiple Image Files from the File/directory */
	@Transactional
	public void removeMultipleImageFileFromStorage(List<String> imagePaths) {
		/*User will send the List<String> of imagePath(url) from ImageDatabase. */
		Path rootPath = Paths.get(ImageUploadPath.PATH.getPath());
			imagePaths
				.forEach( imagePath ->{
					
					Path completeImagePath = rootPath.resolve(imagePath);
					try {
						Files.deleteIfExists(completeImagePath);
						log.info("Image File {} deleted from the Directory ", completeImagePath);
					} catch (IOException e) {
						log.error("Error deleting the Multiple Image Files from the upload Folder.");
					}//ends-catch
				});//ends-loop
	}//ends-method
	
	
	
	
	/* Removes the whole User-folder from the root Directory.
	 *  FolderName is the UserID.
	 */
	@Transactional
	public void removeUserImageFolderFromStorage(int folderName) throws IOException{
		
		String userFolder = String.valueOf(folderName);
		Path completeFolderPath = Paths.get(ImageUploadPath.PATH.getPath()+userFolder);
		if(Files.exists(completeFolderPath)) {
			Files.walk(completeFolderPath)
					.sorted(Comparator.reverseOrder())
					.forEach(path->{
						try {
							Files.delete(path);
						}catch(IOException e) {
							log.error("IOException Occured, cannot delete the folder/Directory : {}", completeFolderPath);
						}
					});
		}
		
		
	}//ends-method
	
	
	
	
}/*ends-class*/

