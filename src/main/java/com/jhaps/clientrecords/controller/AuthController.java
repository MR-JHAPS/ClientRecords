package com.jhaps.clientrecords.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.TokenValidateRequest;
import com.jhaps.clientrecords.dto.request.user.UserAuthRequest;
import com.jhaps.clientrecords.dto.request.user.UserRegisterRequest;
import com.jhaps.clientrecords.dto.response.LoginResponse;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.security.customAuth.AuthService;
import com.jhaps.clientrecords.security.model.CustomUserDetails;
import com.jhaps.clientrecords.service.EmailVerificationService;
import com.jhaps.clientrecords.service.system.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/public")
@Tag(name = "Public API's" , description = "Log-In, refreshToken, Sign-Up API's") // this is for the swagger
public class AuthController {
	
		@Autowired
		private AuthService authService;
		
		@Autowired
		private EmailVerificationService emailVerificationService;
		
		private UserService userService;
		private ApiResponseBuilder apiResponseBuilder;
		
		public AuthController(UserService userService, ApiResponseBuilder apiResponseBuilder) {
			this.userService = userService;
			this.apiResponseBuilder = apiResponseBuilder;
		}
		
	
		
		/**
		 * @returns : LoginResponse
		 * LoginResponse contains the "emailRegistrationStatus" and the "regular-token" and "refresh-token". 
		 */
		
		@Operation(summary = "user Login")
		@PostMapping("/login")
		@PreAuthorize("permitAll()")
		public ResponseEntity<ApiResponseModel<LoginResponse>> userLogin(@Valid @RequestBody UserAuthRequest userAuthRequest){
			log.info("Requesting verification of userLogin Details.");
			LoginResponse responseBody =  authService.verifyUser(userAuthRequest);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, responseBody);
		}
		
		
		@Operation(summary = "user Signup")
		@PostMapping("/signup")
		@PreAuthorize("permitAll()")
		public ResponseEntity<ApiResponseModel<String>> userSignUp(@Valid @RequestBody UserRegisterRequest registrationDto){
			userService.saveNewUser(registrationDto);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.CREATED, "User Created Successfully");
		}
		
		
		
		@Operation(summary = "validate Token")
		@PostMapping("/validate-token")
		@PreAuthorize("permitAll()")
		public ResponseEntity<ApiResponseModel<String>> validateToken(@Valid @RequestBody TokenValidateRequest request,
												@AuthenticationPrincipal UserDetails userDetails){
			log.info("Requesting verification of userLogin Details | PublicController -->'/validateToken' ");
			log.info("Token for validation : {}", request.getToken());
			 authService.validateToken(request.getToken(), userDetails);
			log.info("Inside the tokenValidate controller after token generation : {}", request.getToken());
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "Token is Valid ");
		}
		
		
		
		

		@Operation(summary = "Send Verification Code To Email")
		@GetMapping("/send-verification-email")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<ApiResponseModel<String>> sendVerificationEmail(@AuthenticationPrincipal CustomUserDetails userDetails){
			int userId = userDetails.getUserId();
			emailVerificationService.sendVerificationEmail(userId);
			
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "Verification Email Sent Successfully.");
			
		}
		
		
		
		@Operation(summary = "verify Email")
		@GetMapping("/verify-email")
		@PreAuthorize("isAuthenticated()")
		public ResponseEntity<ApiResponseModel<String>> verifyUserEmail(@RequestParam(name = "verification_code") String verificationCode,
																	@AuthenticationPrincipal CustomUserDetails userDetails){
			int userId = userDetails.getUserId();
			emailVerificationService.verifyUserEmailAddress(userId, verificationCode);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "Email Verified Successfully.");
		}
		
		
		
		
		
}//ends controller



		
/* ------------------------ WILL IMPLEMENT TOKEN LOG OUT WHEN I IMPLEMENT REDIS CACHE ------------------------*/
	
		
		
	/*	
	    @Operation(summary = "user LogOut")
		@PostMapping("/logout")
		public ResponseEntity<ApiResponseModel<String>> userLogin(@Parameter(hidden=true) @RequestHeader("Authorization") String authHeader,
				HttpServletRequest request, HttpServletResponse response,
				@AuthenticationPrincipal UserDetails userDetails){
			log.info("Logging-Out user --------- ");
			authService.logOutUser(authHeader, request, response, userDetails);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "userLogged out successfully");
		}
	*/	
		
/* ------------------------ WILL IMPLEMENT TOKEN LOG OUT WHEN I IMPLEMENT REDIS CACHE ------------------------*/		
		
		

		
		
		
		
		

