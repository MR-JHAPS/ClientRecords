package com.jhaps.clientrecords.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.TokenValidateRequest;
import com.jhaps.clientrecords.dto.request.user.UserAuthRequest;
import com.jhaps.clientrecords.dto.request.user.UserRegisterRequest;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.security.customAuth.AuthService;
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
		
//	@Value("${cloudinary.api-key}")
//	public String cloudinaryApiKey;  
	
		@Autowired
		private AuthService authService;
	
		private UserService userService;
		private ApiResponseBuilder apiResponseBuilder;
		
		public AuthController(UserService userService, ApiResponseBuilder apiResponseBuilder) {
			this.userService = userService;
			this.apiResponseBuilder = apiResponseBuilder;
		}
		
	
		
		@Operation(summary = "user Login")
		@PostMapping("/login")
		@PreAuthorize("permitAll()")
		public ResponseEntity<ApiResponseModel<String>> userLogin(@Valid @RequestBody UserAuthRequest userAuthRequest){
//			log.info("This is the CLOUDINARY_API_KEY : {}. ",cloudinaryApiKey);
			log.info("Requesting verification of userLogin Details.");
			String token = authService.verifyUser(userAuthRequest);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, token);
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
		
		

		
		
		
		
		

