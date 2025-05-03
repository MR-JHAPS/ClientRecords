package com.jhaps.clientrecords.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.user.UserUpdateRequest;
import com.jhaps.clientrecords.dto.request.user.UserImageUploadRequest;
import com.jhaps.clientrecords.dto.response.user.UserGeneralResponse;
import com.jhaps.clientrecords.entity.system.Image;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.security.model.CustomUserDetails;
import com.jhaps.clientrecords.service.system.UserService;
import com.jhaps.clientrecords.util.ImageUploadPath;
import com.jhaps.clientrecords.util.mapper.UserMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Validated
@RestController
@RequestMapping("/api/user")
@Tag(name = "User API's")
public class UserController {

	
	private UserService userService;
	private ApiResponseBuilder apiResponseBuilder;
	private UserMapper userMapper;

	
	
	public UserController(UserService userService, ApiResponseBuilder apiResponseBuilder, UserMapper userMapper) {
		this.userService = userService;
		this.apiResponseBuilder = apiResponseBuilder;
		this.userMapper = userMapper;
	}

	

	
	
	
	@Operation(summary = "Get authenticated user's details")
	@GetMapping("/me")
	public ResponseEntity<ApiResponseModel<UserGeneralResponse>> getUserSelf(@AuthenticationPrincipal CustomUserDetails userDetails) {
		int userId = userDetails.getUser().getId();
		User user = userService.getCurrentUser(userId);
		UserGeneralResponse userGeneralResponse = this.userMapper.toUserGeneralResponse(user);
		

	    // Use image URL if present, else use default image
	    String imagePath = user.getProfileImage()
	            .map(Image::getUrl)
	            .orElse("defaultImage.png");
	    userGeneralResponse.setImage("/images/" + imagePath);
	    return apiResponseBuilder.buildApiResponse(
	            ResponseMessage.SUCCESS,
	            HttpStatus.OK,
	            userGeneralResponse
	    );
	}
	
	
	
	
//	@Operation(summary = "Serve Image")
//	@GetMapping("/images/{imageEndpoint}")
//	public ResponseEntity<Resource> serveImage(@PathVariable String imageEndpoint){
//		log.info("I am inside the serve image.");
//		 Path imageUrl;
//		 if(imageEndpoint.equals("defaultImage.png")) {
//			 imageUrl = Paths.get(ImageUploadPath.PATH.getPath() + imageEndpoint);	
//			 return ResponseEntity.ok()
//			           .contentType(MediaType.IMAGE_PNG)
//			           .body(new FileSystemResource(imageUrl));
//		 }else {
//			 imageUrl = Paths.get(ImageUploadPath.PATH.getPath() + imageEndpoint);	
//			 return ResponseEntity.ok()
//		           .contentType(MediaType.ALL)
//		           .body(new FileSystemResource(imageUrl));
//		    
//		}
//	}
	
//	@Operation(summary = "Serve Image")
//	@GetMapping("/api/images/{imagePath:^(?!.*\\.\\.).*}")
//	public ResponseEntity<Resource> serveImage(@PathVariable String imagePath) {
//	    return serveImageInternal(imagePath);
//	}
//
//	@Operation(hidden = true)
//	@GetMapping("/api/images/{folder}/{filename:.+}")
//	public ResponseEntity<Resource> serveImageNested(@PathVariable String folder, @PathVariable String filename) {
//	    String imagePath = folder + "/" + filename;
//	    return serveImageInternal(imagePath);
//	}
//
//	private ResponseEntity<Resource> serveImageInternal(String imagePath) {
//	    // Prevent directory traversal
//	    if (imagePath.contains("..") || imagePath.contains("\\") || imagePath.startsWith("/")) {
//	        return ResponseEntity.badRequest().build();
//	    }
//
//	    Path fullPath = Paths.get(ImageUploadPath.PATH.getPath(), imagePath).normalize();
//	    File file = fullPath.toFile();
//
//	    if (!file.exists() || !file.isFile()) {
//	        return ResponseEntity.notFound().build();
//	    }
//
//	    MediaType mediaType;
//	    try {
//	        String mimeType = Files.probeContentType(fullPath);
//	        mediaType = (mimeType != null) ? MediaType.parseMediaType(mimeType) : MediaType.APPLICATION_OCTET_STREAM;
//	    } catch (IOException e) {
//	        mediaType = MediaType.APPLICATION_OCTET_STREAM;
//	    }
//
//	    return ResponseEntity.ok()
//	            .contentType(mediaType)
//	            .body(new FileSystemResource(file));
//	}

	

	
	
	
	
	@Operation(summary = "Delete authenticated user's profile")
	@DeleteMapping("/me")
	public ResponseEntity<ApiResponseModel<String>> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails ){			
		int userId = userDetails.getUser().getId();
		log.info("User {} initiated account deletion", userId);
		userService.deleteCurrentUser(userId);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_DELETED, HttpStatus.NO_CONTENT);
	}
	
	
	@Operation(summary = "Update authenticated user's profile" )
	@PutMapping("/me")
	public ResponseEntity<ApiResponseModel<String>> updateAuthenticatedUser(@RequestBody UserUpdateRequest request ,
					@AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUser().getId();
		log.info("User {} initiated account update", userId);
		userService.updateCurrentUser(userId, request);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "User updated Successfully");
	}
	
	
	@Operation(summary = "Upload profile image for authenticated user")
	@PostMapping("/me/image")
	public ResponseEntity<ApiResponseModel<String>> uploadProfileImageOfCurrentUser(@ModelAttribute UserImageUploadRequest request ,
					@AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUser().getId();
		String imageUrlEndpoint = userService.updateCurrentUserProfileImage(userId, request);
		String fullUrl = ImageUploadPath.PATH.getPath() + File.separator + imageUrlEndpoint;
		
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, 
					fullUrl);
	}
	
	
	@Operation(summary = "Remove profile image for authenticated user")
	@DeleteMapping("/me/image")
	public ResponseEntity<ApiResponseModel<String>> removeProfileImageOfCurrentUser(
					@AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUser().getId();
		userService.removeCurrentUserCustomProfileImage(userId);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, 
					"Profile Image removed Successfully.");
	}
	
	
	
//	@Operation(summary = "Get authenticated user's details")
//	@GetMapping("/me")
//	public ResponseEntity<ApiResponseModel<UserGeneralResponse>> getUserSelf(@AuthenticationPrincipal CustomUserDetails userDetails) {
//		int userId = userDetails.getUser().getId();
//		User user = userService.getCurrentUser(userId);
//		UserGeneralResponse userGeneralResponse = this.userMapper.toUserGeneralResponse(user);
//		userGeneralResponse.setImage(ImageUploadPath.PATH.getPath() + user.getProfileImage().get().getUrl());
//		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userGeneralResponse);
//	}
	
	
}// ends class
