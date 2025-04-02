package com.jhaps.clientrecords.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.user.UserAuthRequest;
import com.jhaps.clientrecords.dto.request.user.UserRegisterRequest;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.security.customAuth.AuthService;
import com.jhaps.clientrecords.service.system.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/public")
@Tag(name = "1. Public Controller" , description = "Log-In, refreshToken, Sign-Up API's") // this is for the swagger
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
		public ResponseEntity<ApiResponseModel<String>> userLogin(@Valid @RequestBody UserAuthRequest userAuthRequest){
			log.info("Requesting verification of userLogin Details | PublicController -->'/login' ");
			String token = authService.verifyUser(userAuthRequest);
			log.info("Inside the userLogin controller after token generation : {}", token);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, token);
		}
		
		
		@Operation(summary = "user Signup")
		@PostMapping("/signup")
		public ResponseEntity<ApiResponseModel<String>> userSignUp(@Valid @RequestBody UserRegisterRequest registrationDto){
			userService.saveNewUser(registrationDto);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.CREATED, "User Created Successfully");
		}
		
		
		
		@Operation(summary = "user LogOut")
		@PostMapping("/logout")
		public ResponseEntity<ApiResponseModel<String>> userLogin(@Parameter(hidden=true) @RequestHeader("Authorization") String authHeader,
				HttpServletRequest request, HttpServletResponse response,
				@AuthenticationPrincipal UserDetails userDetails){
			log.info("Logging-Out user --------- ");
			authService.logOutUser(authHeader, request, response, userDetails);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "userLogged out successfully");
		}

		
		
		
		
		
		
//		public ResponseEntity<ApiResponseModel<String>> refreshJwtToken(){
//			
//		}
		
		
		
		
		
		
		
		
}//ends controller
