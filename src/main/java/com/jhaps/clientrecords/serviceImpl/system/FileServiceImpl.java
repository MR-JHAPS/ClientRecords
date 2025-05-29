package com.jhaps.clientrecords.serviceImpl.system;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jhaps.clientrecords.dto.request.FileRequest;
import com.jhaps.clientrecords.dto.request.user.UserImageUploadRequest;
import com.jhaps.clientrecords.dto.response.FileResponse;
import com.jhaps.clientrecords.entity.system.UserFile;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.exception.system.FileDeletionException;
import com.jhaps.clientrecords.exception.system.FileException;
import com.jhaps.clientrecords.exception.system.FileNotFoundException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.system.FileRepository;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.service.CloudinaryService;
import com.jhaps.clientrecords.service.system.FileService;
import com.jhaps.clientrecords.util.CustomFileManager;
import com.jhaps.clientrecords.util.mapper.FileMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class FileServiceImpl implements FileService{

	private FileRepository fileRepo;
	private UserRepository userRepo;
	private CustomFileManager customFileManager; // helps generate random customFileName .
	private FileMapper fileMapper;
	private CloudinaryService cloudinaryService;
	
	
/*--------------------------------------------------- Private method of user for images ------------------------------------------------------------*/
	
	private User findUserById(int id) {
		return userRepo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User with id : " + id + " not found"));
	}
	
/*--------------------------------------------------------------------------------------------------------------------------------------------------*/	
	
	
	
	
	@Override
	public FileResponse getFileById(int imageId, int userId) {
		log.info("Action: Getting image By Image_Id: {}", imageId);
		UserFile userFile = fileRepo.findByIdAndUserId(imageId, userId)
				.orElseThrow (() -> {
					log.error("Image with imageID {} and userId {} not found in the database.", imageId, userId);
					throw new FileNotFoundException("Image with Image-Id: " + imageId + " and userId "+ userId +" not found.");
				});
		log.info("Image with imageID {} and userID {} fetched Successfully", imageId, userId);
		String signedUrl = cloudinaryService.getSignedUrl(userFile.getUser().getId(), userFile.getStoredFileName());
		FileResponse response = fileMapper.toFileResponse(userFile, signedUrl);
		return response;
	}
	
	
	@Override
	public Page<FileResponse> getFilesOfCurrentUser(int userId, Pageable pageable){
		log.info("Getting images for User of ID : ", userId);
		Page<UserFile> userFiles = fileRepo.findByUser_Id(userId , pageable);
		
		if(userFiles.isEmpty()) {
			log.info("Message: Files for User_ID : {} not found in Database.", userId);
		}else {
			log.info("Message: Fetched {} Files for User_ID : {} fetched.", userFiles.getNumberOfElements(), userId);	
		}
		
		/**
		 * @UserFile is entity it does not have the fileUrl
		 * @cloudinaryService.getSignedUrl - return SignedUrl as String.
		 * @FileResponse has FileUrl field so we are mapping UserFile->infos , String url and putting it in FileResponse->FileURL
		 * @returns List<FileResponse>.
		 * */		
		List<FileResponse> responseList = userFiles.stream()
												.map(file -> {
													String signedUrl = cloudinaryService.getSignedUrl(file.getUser().getId(), file.getStoredFileName());
													return fileMapper.toFileResponse(file, signedUrl);
												})
												.collect(Collectors.toList());
		
		/**
		 * Converting List<FileResponse> to Page<FileResponse>
		 * @responseList : is the List<FileResponse> 
		 * @pageable : is the argument received in this method.
		 * @userFiles.getNumberOfElements is the total number of contents.
		 * @return : Page<FileResponse> including signed File URL.
		 * */
		return new PageImpl<FileResponse>(responseList, pageable, userFiles.getNumberOfElements()); 


	}
	

	
	@Override
	@Transactional
	public UserFile saveFile(int userId,FileRequest request) {
		log.info("Action: Saving File: {}", request.getFileName());
		User user = findUserById(userId);
		MultipartFile imageFile = request.getFile();
		String originalImageName = request.getFileName();
		String contentType = imageFile.getContentType();
		String customImageName = customFileManager.getCustomFileName(user);
		String folderName = String.valueOf(userId);
		Map<String, String> cloudinaryUploadDetails = cloudinaryService.uploadFile(imageFile, folderName, customImageName);
		String publicId = cloudinaryUploadDetails.get("publicId");
		UserFile userFile = UserFile.builder()
							.fileName(originalImageName)
							.storedFileName(customImageName)
							.contentType(contentType)
							.uploadTime(LocalDateTime.now())
							.publicId(publicId)
							.user(user)
							.build();
		log.info("Action: File: {} saved successfully by User_Email: {}.", request.getFileName(), user.getEmail());
		fileRepo.save(userFile);
		return userFile;
	}
	

	
	
	
	
	@Override
	public UserFile updateProfileImage(UserImageUploadRequest request, int userId) {
		if(request.getImageFile()==null) {
			log.warn("Image Multipart is null or not found in the UserImageUploadRequest.");
			throw new FileException("Image Multipart not found in the request");
		}
		User user = findUserById(userId);
		MultipartFile imageFile = request.getImageFile();
		String contentType = request.getImageFile().getContentType();
		String folderName = String.valueOf(userId);

		String originalImageName = request.getImageName();
		String customImageName = customFileManager.getCustomFileName(user); //generates a custom fileName to save in DB.
		Map<String, String> cloudinaryUploadDetails = cloudinaryService.uploadFile(imageFile, folderName, customImageName);
		String publicId = cloudinaryUploadDetails.get("publicId");

		/* Checking if the Image with "given name for current-user" Already exists in the Database*/
		boolean existsOnDb = fileRepo.existsByFileNameAndUser_Id(originalImageName, userId);
		
		if(existsOnDb) {
			UserFile imageOnDb = fileRepo.findByFileNameAndUser_Id(originalImageName, userId);
			log.info("File already Exists on Database. Fetching the existing file and setting as profile picture");
			return imageOnDb;
		}
		
		UserFile userFile = UserFile.builder()
							.fileName(originalImageName)
							.storedFileName(customImageName)
							.contentType(contentType)
							.uploadTime(LocalDateTime.now())
							.publicId(publicId)
							.user(user)
							.build();
		
		fileRepo.save(userFile);
		log.info("Image {} with custom name {} saved successfully in the Image Repository for user {}.",
							originalImageName, customImageName, user.getEmail());
		return userFile;
	}//ends method
	
	
	
	
	
	
	/*
	 * Deletes image by id only if it belongs to the  current-authenticated-user.
	 * And if the profile image is selected to delete it sets the current user profile
	 * to DefaultImage("defaultImage.png") and then deletes it to prevent Foreign Key Constraints issues.
	 */
	@Override
	@Transactional
	public void deleteFileById(int fileId, int userId) {
		log.info("Action: Deleting Image of Id: {}", fileId);
		if(!fileRepo.existsByIdAndUserId(fileId, userId)) {
			 log.warn("Delete failed: File {} not found or not owned by user {}", fileId, userId);
			throw new FileNotFoundException("File with Id: " + fileId + " not found or you do not have permission to delete.");	
		}
		/* If File id selected to delete is userProfile than we need to set the default profile picture before deleting the image.*/
		UserFile fileToDelete = findFileById(fileId);
		User currentUser = findUserById(userId);
		User fileOwner = fileToDelete.getUser();
		if(fileOwner.getId() != currentUser.getId()) {
			log.warn("Not Authorized to delete File of id: {} by user: {}. File doesn't belong to user{}",
					fileToDelete.getId(),currentUser.getId(), currentUser.getEmail());
			throw new FileDeletionException("Not Authorized to delete the File. You are not the owner of the File.");
		}
		
		/*
		 * Deleting the image from the FileDirectory.
		 * @Args: imageToDelete.getUrl() is the path of image inside RootDirectory.
		 */
//		Getting PublicId of the Image this is needed to delete the image from the cloudinary.
		String publicId = fileToDelete.getPublicId();
		cloudinaryService.deleteSingleFile(publicId);
		fileRepo.deleteByIdAndUserId(fileId, userId);
		log.info("Action: File of Id:{} deleted Successfully.", fileId);
	}
	


	
	/*
	 * Deletes Multiple files by id only if it belongs to the current-authenticated-user.
	 */
	@Override
	@Transactional
	public void deleteMultipleFilesById(List<Integer> fileIdList, int userId) {
		log.info("Action: Deleting Multiple Files of Id's: {}", fileIdList);	
		User currentUser = findUserById(userId);
			
		
		try{
			/* Checking if the user has a profile Picture first.*/
			if(currentUser.getProfileImage().isPresent()) {
				int userProfileImageId = currentUser.getProfileImage().get().getId();
			
				for(Integer fileId : fileIdList) {
					/* Checking if the id in imageIdList contains the userProfileImage ---> imageId. */
						if(fileId.equals(userProfileImageId)) {
							log.info("Selected Image {} is also a userProfile Image.", fileId);
							currentUser.setProfileImage(null);
							log.info("Setting the user profileImage as null");
							userRepo.saveAndFlush(currentUser);
							log.info("Saving the userProfile After setting image to null.");
						}//ends-if
				}//ends-for
			
			}//ends-if
			
		   /*
			* Gets the FilePublicIdPath from the FileRepo using List<Integer> fileIds and userId. 
			*/
			List<String> filePublicIdList = fileRepo.findPublicIdByFileIds(fileIdList, userId);

			cloudinaryService.deleteMultipleFiles(filePublicIdList);
			
			/*
			 * Deleting the files from the fileRepo after removing from the Database.
			 */
			int totalDeleted = fileRepo.deleteByIdsAndUserId(fileIdList, userId);
			
			log.info("Action: Total {} Files, that of Id's:{} deleted Successfully.", totalDeleted, fileIdList);			
		}catch (Exception e) {
			log.error("Unable to Delete Multiple files with id's {}, of userId {}", fileIdList, userId);
			throw new FileDeletionException("Error occured. Unable to delete Mulptiple Files of Id's : " + fileIdList + " of userId : "+ userId);
		}
	}
	
	
	
	
	/* Deletes all the images of given user from the imageRepository. */
	@Override
	@Transactional
	public void deleteAllFilesOfGivenUser(int userId) {		
		try{
//			customFileManager.removeUserImageFolderFromStorage(userId);
			fileRepo.deleteAllFilesByUserId(userId);
			log.info("Delete all files with userId successful.");
		}catch (Exception e) {
			log.error("Error deleting images for user {}", userId, e);
			throw new DataIntegrityViolationException("Delete All images by user id failed.");
		}
	}

	
	
	
	/* Private Method to find file By id: */
	private UserFile findFileById(int fileId) {
		return fileRepo.findById(fileId)
					.orElseThrow(()-> new FileNotFoundException("Error: File with ID:" + fileId + " not found . "));
	}
	
	
	
	
	
	
	
	
	
	
	
}//ends class.
