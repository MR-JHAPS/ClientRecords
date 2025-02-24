package com.jhaps.clientrecords.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.response.ApiResponse;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.response.ResponseMessage;
import com.jhaps.clientrecords.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/public")
//@Validated //this is the method validation so that i can use it on pathvariable.
@Tag(name = "Public Controller" , description = "Log-In, Sign-Up API's") // this is for the swagger
public class PublicController {

	private UserService userService;
	private ApiResponseBuilder apiResponseBuilder;
	
	public PublicController(UserService userService, ApiResponseBuilder apiResponseBuilder) {
		this.userService = userService;
		this.apiResponseBuilder = apiResponseBuilder;
	}
	
	
	//We get the JwtToken from this controller after successful Login.
	@Operation(summary = "user Login")
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<String>> userLogin(@Valid @RequestBody UserDto userDto){
		String token = userService.verifyUser(userDto);
		if(token!=null) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, token);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.UNAUTHORIZED, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@Operation(summary = "user Signup")
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Object>> userSignUp(@Valid @RequestBody UserDto userDto){
		try {
			userService.saveUser(userDto);
				return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.CREATED, "User Created Successfully");
			}catch (Exception e) {
				return apiResponseBuilder.buildApiResponse(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	
	
	
	
	
	
	
	
	
}//ends class
