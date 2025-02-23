package com.jhaps.clientrecords.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.response.ApiResponse;
import com.jhaps.clientrecords.response.ResponseMessage;
import com.jhaps.clientrecords.service.JWTServiceImpl;
import com.jhaps.clientrecords.service.UserService;
import com.jhaps.clientrecords.util.ApiResponseBuilder;

import jakarta.validation.Valid;

//@CrossOrigin(origins = "http://localhost:4209") // Allow Angular frontend

@RestController
@RequestMapping("/user")
public class UserController {

	private JWTServiceImpl jwtService;
	
	private AuthenticationManager authManager;
	
	private PasswordEncoder passwordEncoder;
	
	private UserService userService;
	
	private ApiResponseBuilder apiResponseBuilder;
	
	
	public UserController(PasswordEncoder passwordEncoder, UserService userService, 
							JWTServiceImpl jwtService, AuthenticationManager authManager,
							ApiResponseBuilder apiResponseBuilder) {
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
		this.jwtService = jwtService;
		this.authManager= authManager;
		this.apiResponseBuilder = apiResponseBuilder;
	}



	
	
	
	
	//we are trying to print the bearer/JWT token in postman console so return type is String
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<String>> userLogin(@Valid @RequestBody User user){
		String token = userService.verifyUser(user);
		if(token!=null) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, token);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.UNAUTHORIZED, HttpStatus.NOT_FOUND);
		}

	}
	
	
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		
		List<User> userList = userService.findAllUsers();
		
		if(userList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}	
		return ResponseEntity.ok(userList);
		
	}
	

	@GetMapping("/id/{id}")
	public ResponseEntity<User> getUserById(@PathVariable int id) {
	
		return userService.findUserById(id)
					.map(ResponseEntity::ok)
					.orElse(ResponseEntity.notFound().build());
	}
	
	
	
	@GetMapping("/role/{role}")
	public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role){
		
		List<User> userList = userService.findUsersByRoleName(role);
		if(userList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(userList);
		
		
	}
	
//	@GetMapping("/email/{email}")
//	public ResponseEntity<User> getUsersByEmail(@PathVariable String email){
//		
//		return userService.findUsersByRoleName(email)
//				.map(ResponseEntity::ok)
//				.orelse(ResponseEntity.notFound().build());
//	}
	
	
	
	
//	@GetMapping("/id/{id}")
//	public ResponseEntity<User> getUserById(@PathVariable int id) {
//		
//		Optional<User> user = userService.findUserById(id);
//		
//		//optional is never null so just checking for empty/present.
//		if(user.isEmpty() ) {
//			return ResponseEntity.notFound().build();
//		}	else {
//			//if user.isPresent() then we will do user.get() because user-->is optional a return type is just a user.
//			//converting from optionalUser to user.
//		return ResponseEntity.ok(user.get());
//		}
//	}  //OR
	
	
	
	
	
	
	
	
	
	
	
	
}// ends class
