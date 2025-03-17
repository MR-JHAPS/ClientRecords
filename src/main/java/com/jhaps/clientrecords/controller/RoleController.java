package com.jhaps.clientrecords.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.RoleDto;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.response.ApiResponseModel;
import com.jhaps.clientrecords.service.RoleService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role Controller", description = "Manages all the operations related to Roles")
public class RoleController {

	private RoleService roleService;
	
	private ApiResponseBuilder apiResponseBuilder;
	
	public RoleController(RoleService roleService, ApiResponseBuilder apiResponseBuilder) {
		this.roleService = roleService;
		this.apiResponseBuilder = apiResponseBuilder;
	}
	
	@GetMapping("/roles")
	public ResponseEntity<ApiResponseModel<String>> getAllRoles(){
		List<RoleDto> roleList = roleService.
	}
	
	
	
	
	
	
	
	
	
	
}//ends controller
