package com.jhaps.clientrecords.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.security.model.CustomUserDetails;
import com.jhaps.clientrecords.service.system.UserService;
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
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userGeneralResponse);
	}
	
	
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
	public ResponseEntity<ApiResponseModel<String>> uploadProfileImageOfCurrentUser(@RequestBody UserImageUploadRequest request ,
					@AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUser().getId();
		userService.updateCurrentUserProfileImage(userId, request);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, 
					"Image " + request.getImageName() + " saved Successfully");
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
	
	
	
	
	
}// ends class
