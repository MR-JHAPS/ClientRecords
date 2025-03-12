package com.jhaps.clientrecords.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.response.ApiResponseModel;
import com.jhaps.clientrecords.service.PagedResourceAssemblerService;
import com.jhaps.clientrecords.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private UserService userService;
	
	private ApiResponseBuilder apiResponseBuilder;
	
	private PagedResourceAssemblerService<UserDto> pagedResourceAssemblerService;
	
	
	public AdminController(UserService userService, ApiResponseBuilder apiResponseBuilder,
			PagedResourceAssemblerService<UserDto> pagedResourceAssemblerService) {
		this.userService = userService;
		this.apiResponseBuilder = apiResponseBuilder;
		this.pagedResourceAssemblerService = pagedResourceAssemblerService;
	}
	
	//CONTROLLERS------------------------------------
	
	
	@Operation(summary = "Get List Of All The Users")
	@GetMapping("/findAll")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<UserDto>>>> getAllUsers(@RequestParam(defaultValue="0") int pageNumber,
																						@RequestParam(defaultValue="10") int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<UserDto> paginatedUsers = userService.findAllUsers(pageable);
		PagedModel<EntityModel<UserDto>> pagedUserModel = pagedResourceAssemblerService.toPagedModel(paginatedUsers);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedUserModel);
	}
	

	
	@Operation(summary = "Get List Of  Users By Role Name")
	@GetMapping("/role/{role}")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<UserDto>>>> getUsersByRole(@PathVariable String role,
																				@RequestParam(defaultValue="0") int pageNumber,
																				@RequestParam(defaultValue="10") int pageSize){
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<UserDto> paginatedUsers = userService.findUsersByRoleName(role, pageable);
		PagedModel<EntityModel<UserDto>> pagedUserModel = pagedResourceAssemblerService.toPagedModel(paginatedUsers);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedUserModel);
	}
	
		
	
	
	
	
	
}//ends controller
