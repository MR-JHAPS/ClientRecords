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
import com.jhaps.clientrecords.dto.response.RoleDto;
import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.system.RoleService;
import com.jhaps.clientrecords.util.Mapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role Controller", description = "Manages all the operations related to Roles")
public class RoleController {

	private RoleService roleService;
	
	private ApiResponseBuilder apiResponseBuilder;
	
	private Mapper mapper;
	
	public RoleController(RoleService roleService, ApiResponseBuilder apiResponseBuilder, Mapper mapper) {
		this.roleService = roleService;
		this.apiResponseBuilder = apiResponseBuilder;
		this.mapper = mapper;
	}
	
	@Operation(summary = "Get all roles")
	@GetMapping
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<RoleDto>> getAllRoles(){
		Set<Role> role = roleService.findAllRoles();
		RoleDto roleDto = mapper.toRoleDtoFromRoleSet(role); //converting Set<Role> to RoleDto.
		return apiResponseBuilder
					.buildApiResponse(ResponseMessage.ROLE_OBTAINED, HttpStatus.OK, roleDto);
	}
	
	
	@Operation(summary = "Save new role")
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> saveNewRole(@RequestBody @Valid RoleDto roleDto){
		roleService.saveNewRole(roleDto);
		return apiResponseBuilder
					.buildApiResponse(ResponseMessage.ROLE_SAVED, HttpStatus.OK, "Role : " +roleDto.getRoleNames() + " saved successfully");
	}
	
	
	@Operation(summary = "Delete role")
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<?> deleteRole(@PathVariable int id){
		roleService.deleteRole(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //no custom response because no content does not send Response body.
	}
	
	
	
	
	
}//ends controller
