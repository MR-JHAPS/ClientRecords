package com.jhaps.clientrecords.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.system.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@Validated
@RestController
@RequestMapping("/api/user")
@Tag(name = "User API's")
public class UserController {

	
	private UserService userService;
	private ApiResponseBuilder apiResponseBuilder;

	
	
	public UserController(UserService userService, ApiResponseBuilder apiResponseBuilder) {
		this.userService = userService;
		this.apiResponseBuilder = apiResponseBuilder;
	}

	
	
	@Operation(summary = "Get authenticated user's details")
	@GetMapping("/me")
	public ResponseEntity<ApiResponseModel<UserGeneralResponse>> getUserSelf(@AuthenticationPrincipal UserDetails userDetails) {
		String email = userDetails.getUsername();
		UserGeneralResponse userGeneralResponse = userService.findUserDtoByEmail(email);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userGeneralResponse);
	}
	
	
	@Operation(summary = "Delete authenticated user's profile")
	@DeleteMapping("/delete/me")
	public ResponseEntity<ApiResponseModel<String>> deleteUser(@AuthenticationPrincipal UserDetails userDetails ){			
		String email = userDetails.getUsername();
		System.out.println("Controller : user with email: " + email + " is deleting the user account");
		userService.deleteUserByEmail(email);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_DELETED, HttpStatus.NO_CONTENT);
	}
	
	
	@Operation(summary = "Update authenticated user's profile" )
	@PutMapping("/update/me")
	public ResponseEntity<ApiResponseModel<String>> updateAuthenticatedUser(@RequestBody UserUpdateRequest request ,
					@AuthenticationPrincipal UserDetails userDetails){
		String email = userDetails.getUsername();
		userService.updateUserByEmail(email, request);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "User with email " + email + " updated Successfully");
	}
	
	
	@Operation(summary = "Upload profile image for authenticated user")
	@PostMapping("/image/save")
	public ResponseEntity<ApiResponseModel<String>> uploadImage(@RequestBody UserImageUploadRequest request ,
					@AuthenticationPrincipal UserDetails userDetails){
		String email = userDetails.getUsername();
		userService.updateUserProfileImage(email, request);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, 
					"Image with name " + request.getImageName() + " for user " + email + " saved Successfully");
	}
	
	
	
}// ends class
