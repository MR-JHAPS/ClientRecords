package com.jhaps.clientrecords.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.response.ApiResponse;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.response.ResponseMessage;
import com.jhaps.clientrecords.service.JWTServiceImpl;
import com.jhaps.clientrecords.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//@CrossOrigin(origins = "http://localhost:4209") // Allow Angular frontend

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
public class UserController {

//	private JWTServiceImpl jwtService;
//	
//	private AuthenticationManager authManager;
//	
//	private PasswordEncoder passwordEncoder;
	
	private UserService userService;
	
	private ApiResponseBuilder apiResponseBuilder;
	
	
	public UserController(UserService userService, ApiResponseBuilder apiResponseBuilder) {
		this.userService = userService;
		this.apiResponseBuilder = apiResponseBuilder;
	}


//	//we are trying to print the bearer/JWT token in postman console so return type is String
//	@PostMapping("/login")
//	public ResponseEntity<ApiResponse<String>> userLogin(@Valid @RequestBody User user){
//		String token = userService.verifyUser(user);
//		if(token!=null) {
//			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, token);
//		}else {
//			return apiResponseBuilder.buildApiResponse(ResponseMessage.UNAUTHORIZED, HttpStatus.NOT_FOUND);
//		}
//
//	}
//	
	
	
	
	
	
	
	
	
	
	//THIS SHOULD BE AUTHORIZED ONLY FOR ADMIN WILL CHANGE THIS LATER WHEN I CREATE AN ADMIN CONTROLLER.
	@Operation(summary = "Get List Of All The Users")
	@GetMapping("/findAll")
	public ResponseEntity<ApiResponse<Object>> getAllUsers() {
		List<UserDto> userList = userService.findAllUsers();
		if(userList!=null && !userList.isEmpty()) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userList);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	@Operation(summary = "Get User By ID")
	@GetMapping("/id/{id}")
	public ResponseEntity<ApiResponse<Object>> getUserById(@PathVariable int id) {
		Optional<UserDto> userDto = userService.findUserById(id);
		if(userDto.isPresent()) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userDto);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	//THIS SHOULD BE AUTHORIZED ONLY FOR ADMIN WILL CHANGE THIS LATER WHEN I CREATE AN ADMIN CONTROLLER.
	@Operation(summary = "Get List Of  Users By Role Name")
	@GetMapping("/role/{role}")
	public ResponseEntity<ApiResponse<Object>> getUsersByRole(@PathVariable String role){
		List<UserDto> userList = userService.findUsersByRoleName(role);
		if(userList!=null && !userList.isEmpty()) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userList);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}	
	}
	
	
	
	
	
	
	
	
}// ends class
