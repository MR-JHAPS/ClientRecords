package com.jhaps.clientrecords.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.RoleSaveRequest;
import com.jhaps.clientrecords.dto.response.RoleResponse;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.system.RoleService;
import com.jhaps.clientrecords.util.mapper.RoleMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role API's", description = "Manages all the operations related to Roles")
public class RoleController {

	private RoleService roleService;
	private ApiResponseBuilder apiResponseBuilder;
	
	public RoleController(RoleService roleService, ApiResponseBuilder apiResponseBuilder) {
		this.roleService = roleService;
		this.apiResponseBuilder = apiResponseBuilder;
	}
	
	@Operation(summary = "Get all roles")
	@GetMapping
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<Set<RoleResponse>>> getAllRoles(){
		Set<RoleResponse> roles = roleService.findAllRoles();
		return apiResponseBuilder.buildApiResponse(ResponseMessage.ROLE_OBTAINED, HttpStatus.OK, roles);
	}
	
	
	@Operation(summary = "Save new role")
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> saveNewRole(@RequestBody @Valid RoleSaveRequest roleSaveRequest){
		roleService.saveNewRole(roleSaveRequest);
		return apiResponseBuilder
				.buildApiResponse(ResponseMessage.ROLE_SAVED, HttpStatus.OK, "Role : " +roleSaveRequest.getRole() + " saved successfully");
	}
	
	
	@Operation(summary = "Delete role")
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<?> deleteRole(@PathVariable int id){
		roleService.deleteRole(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //no custom response because no content does not send Response body.
	}
	
	
	
	
	
}//ends controller
