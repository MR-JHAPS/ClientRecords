package com.jhaps.clientrecords.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.response.ApiResponseModel;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.service.PagedResourceAssemblerService;
import com.jhaps.clientrecords.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//@CrossOrigin(origins = "http://localhost:4209") // Allow Angular frontend

@Validated
@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
public class UserController {

	
	private UserService userService;
	
	private ApiResponseBuilder apiResponseBuilder;

	
	
	public UserController(UserService userService, ApiResponseBuilder apiResponseBuilder) {
		this.userService = userService;
		this.apiResponseBuilder = apiResponseBuilder;
	}

	

	
	@Operation(summary = "Get User By ID")
	@GetMapping("/id/{id}")
	public ResponseEntity<ApiResponseModel<UserDto>> getUserById(@PathVariable int id) {
		UserDto userDto = userService.findUserDtoById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userDto);
	}
	
	
	@Operation(summary = "Delete User By ID")
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("#id==principal.id") //comparing the pathVariable id with principal.id(Logged in user id stored in spring security)
	public ResponseEntity<ApiResponseModel<String>> deleteUserById(@PathVariable int id ){		
		userService.deleteUserById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_DELETED, HttpStatus.NO_CONTENT, "User with Id " + id + " deleted Succesfully" );
	}
	
	
	@Operation(summary = "Update User By ID")
	@PutMapping("/update/{id}")
	@PreAuthorize("#id==principal.id")
	public ResponseEntity<ApiResponseModel<String>> updateUserById(@PathVariable int id, @RequestBody UserDto userDto ){
		userService.updateUserById(id, userDto);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "User with id" + id + " updated Successfully");
	}
	

	
}// ends class
