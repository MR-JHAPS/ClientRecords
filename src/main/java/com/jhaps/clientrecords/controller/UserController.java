package com.jhaps.clientrecords.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.user.UserUpdate;
import com.jhaps.clientrecords.dto.response.ImageDto;
import com.jhaps.clientrecords.dto.response.user.UserGeneralDto;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.system.ImageService;
import com.jhaps.clientrecords.service.system.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@Validated
@RestController
@RequestMapping("/api/user")
@Tag(name = "User Controller")
public class UserController {

	
	private UserService userService;
	private ImageService imageService;
	private ApiResponseBuilder apiResponseBuilder;

	
	
	public UserController(UserService userService, ApiResponseBuilder apiResponseBuilder) {
		this.userService = userService;
		this.apiResponseBuilder = apiResponseBuilder;
	}

	
	
	@Operation(summary = "Get User By current logged on user")
	@GetMapping("/me")
	public ResponseEntity<ApiResponseModel<UserGeneralDto>> getUserSelf(@AuthenticationPrincipal UserDetails userDetails) {
		String email = userDetails.getUsername();
		UserGeneralDto userGeneralDto = userService.findUserDtoByEmail(email);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userGeneralDto);
	}
	
	
	
	
//	@Operation(summary = "Get User along with Roles By ID")
//	@GetMapping("/id/{id}/details")
//	@PreAuthorize("hasAuthority('admin')")
//	public ResponseEntity<ApiResponseModel<UserAdminDto>> getUserWithRolesById(@PathVariable int id) {
//		UserAdminDto userAdminDto = userService.findUserWithRolesById(id);
//		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userAdminDto);
//	}
	
	
	@Operation(summary = "Delete current user using userDetails from SecurityContextHolder")
	@DeleteMapping("/delete/me")
	public ResponseEntity<ApiResponseModel<String>> deleteUserById(@AuthenticationPrincipal UserDetails userDetails ){			
		String email = userDetails.getUsername();
		System.out.println("Controller : user with email: " + email + " is deleting the user account");
		userService.deleteUserByEmail(email);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_DELETED, HttpStatus.NO_CONTENT);
	}
	
	
	@Operation(summary = "Update User By ID")
	@PutMapping("/update/me")
	public ResponseEntity<ApiResponseModel<String>> updateUserByEmail(@RequestBody UserUpdate userUpdate ,
					@AuthenticationPrincipal UserDetails userDetails){
		String email = userDetails.getUsername();
		userService.updateUserByEmail(email, userUpdate);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "User with email " + email + " updated Successfully");
	}
	
	
	@Operation(summary = "Save Image of User using userEmail")
	@PutMapping("/image/save")
	public ResponseEntity<ApiResponseModel<String>> uploadImage(@RequestBody ImageDto imageDto ,
					@AuthenticationPrincipal UserDetails userDetails){
		String email = userDetails.getUsername();
		imageService.saveImage(email, imageDto);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, 
					"Image with name " + imageDto.getImageName() + " for user " + email + " saved Successfully");
	}
	
}// ends class
