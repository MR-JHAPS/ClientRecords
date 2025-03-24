package com.jhaps.clientrecords.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.response.ApiResponseModel;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.service.AuthService;
import com.jhaps.clientrecords.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/public")
@Tag(name = "Public Controller" , description = "Log-In, refreshToken, Sign-Up API's") // this is for the swagger
public class AuthController {

	
		@Autowired
		private AuthService authService;
	
		private UserService userService;
		private ApiResponseBuilder apiResponseBuilder;
		
		public AuthController(UserService userService, ApiResponseBuilder apiResponseBuilder) {
			this.userService = userService;
			this.apiResponseBuilder = apiResponseBuilder;
		}
		
		
		/*We get userDto(email, password) and return the JWT token as response if credentials are Correct. */
		@Operation(summary = "user Login")
		@PostMapping("/login")
		//@PreAuthorize("permitAll()")
		public ResponseEntity<ApiResponseModel<String>> userLogin(@Valid @RequestBody UserDto userDto){
			log.info("Requesting verification of userLogin Details | PublicController -->'/login' ");
			String token = authService.verifyUser(userDto);
			log.info("inside the userLogin controller after token generation : {}", token);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, token);
		}
		
		
		@Operation(summary = "user Signup")
		@PostMapping("/signup")
		public ResponseEntity<ApiResponseModel<String>> userSignUp(@Valid @RequestBody UserDto userDto){
			userService.saveNewUser(userDto);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.CREATED, "User Created Successfully");
		}
		
		
		
//		public ResponseEntity<ApiResponseModel<String>> refreshJwtToken(){
//			
//		}
		
		
		
		
		
		
		
		
}//ends controller
