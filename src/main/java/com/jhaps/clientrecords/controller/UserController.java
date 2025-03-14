package com.jhaps.clientrecords.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.CustomUserDetails;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.response.ApiResponseModel;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.service.PagedResourceAssemblerService;
import com.jhaps.clientrecords.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

//@CrossOrigin(origins = "http://localhost:4209") // Allow Angular frontend

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
public class UserController {

	
	private UserService userService;
	
	private ApiResponseBuilder apiResponseBuilder;
	
	private PagedResourceAssemblerService<UserDto> pagedResourceAssemblerService;
	
	
	public UserController(UserService userService, ApiResponseBuilder apiResponseBuilder,
			PagedResourceAssemblerService<UserDto> pagedResourceAssemblerService) {
		this.userService = userService;
		this.apiResponseBuilder = apiResponseBuilder;
		this.pagedResourceAssemblerService = pagedResourceAssemblerService;
	}

	

	
	@Operation(summary = "Get User By ID")
	@GetMapping("/id/{id}")
	public ResponseEntity<ApiResponseModel<UserDto>> getUserById(@PathVariable int id) {
		UserDto userDto = userService.findUserDtoById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userDto);
	}
	
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("#id==principal.id")
	public ResponseEntity<ApiResponseModel<String>> deleteUserById(@PathVariable int id ){

//		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		CustomUserDetails customUserDetails = (CustomUserDetails)auth.getPrincipal();
//		
//		String loggedInUser = customUserDetails.getUsername();
//		Set<String> rolesList = customUserDetails.getAuthorities()
//										.stream()
//										.map(GrantedAuthority::getAuthority)
//										.collect(Collectors.toSet());
		
//		System.out.println("This is currently logged in user " + loggedInUser);
//		System.out.println("This are the roles of the current active user : " + rolesList);
//		System.out.println(principal.getName());
//		System.out.println("The role of the current user is : " + auth.getAuthorities());
		
		userService.deleteUserById(id); //getting user Email from Principal (SpringSecurity).
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_DELETED, HttpStatus.NO_CONTENT, "User with Id " + id + " deleted Succesfully" );
	}
	
	
	
	
	
	
	
	
	
	
}// ends class
