package com.jhaps.clientrecords.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.RoleRequest;
import com.jhaps.clientrecords.dto.request.user.AdminUpdateRequest;
import com.jhaps.clientrecords.dto.response.user.UserAdminResponse;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.system.AdminService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@Validated
@Tag(name = "Admin API's" , description = "Manage Users, Manage Clients, Manage Roles")
public class AdminController {

	private ApiResponseBuilder apiResponseBuilder;	
	private AdminService adminService;
	private PagedResourceAssemblerService<UserAdminResponse> pagedResourceAssemblerService;
	
	public AdminController(ApiResponseBuilder apiResponseBuilder, AdminService adminService,
			PagedResourceAssemblerService<UserAdminResponse> pagedResourceAssemblerService) {
		this.apiResponseBuilder = apiResponseBuilder;
		this.pagedResourceAssemblerService = pagedResourceAssemblerService;
		this.adminService = adminService;
	}
	
	
	
	
	@Operation(summary = "Get List Of All The Users")
	@GetMapping("/user/find-all")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<UserAdminResponse>>>> getAllUsers(
								@RequestParam(defaultValue="0") int pageNumber,
								@RequestParam(defaultValue="10") int pageSize,
								@RequestParam(required = false) String sortBy,
								@RequestParam(required = false) String direction
								){
		Pageable pageable =  PageableUtils.createPageable(pageNumber, pageSize, sortBy, direction);
		Page<UserAdminResponse> paginatedUsers = adminService.findAllUsers(pageable);
		PagedModel<EntityModel<UserAdminResponse>> pagedUserModel = pagedResourceAssemblerService.toPagedModel(paginatedUsers);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedUserModel);
	}
	
	
	@Operation(summary = "Get User along with Roles By ID")
	@GetMapping("/user/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<UserAdminResponse>> getUserWithRolesByUserId(@PathVariable int id) {
		UserAdminResponse userAdminResponse = adminService.findUserWithRolesById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userAdminResponse);
	}
	
	
	@Operation(summary = " Update Admin info ")
	@PutMapping("/update/me")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> updateAdmin(@RequestBody AdminUpdateRequest adminUpdateRequest,
											@AuthenticationPrincipal UserDetails userDetails){
		String email = userDetails.getUsername();
		adminService.updateAdmin(email, adminUpdateRequest);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.ADMIN_UPDATED, HttpStatus.OK);
	}

	

	@Operation(summary = "Search user by user Email", description = "User-Email is unique so it will return only one data.")
	@GetMapping("/search-by/email")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<UserAdminResponse>> getUserWithRolesByUserEmail(@RequestParam String email) {
		UserAdminResponse userAdminResponse = adminService.searchUserByEmail(email);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userAdminResponse);
	}
	
	@Operation(summary = " Delete User By Id ")
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> deleteUserById(@PathVariable int id){
		adminService.deleteUserById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_DELETED, HttpStatus.NO_CONTENT);
	}
	
	
	@Operation(summary = "Get List Of Users By Role Name")
	@GetMapping("/search-by/role")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<UserAdminResponse>>>> getUsersByRole(
												@NotBlank @RequestParam String role,
												@RequestParam(defaultValue="0") int pageNumber,
												@RequestParam(defaultValue="10") int pageSize,
												@RequestParam(required = false) String sortBy,
												@RequestParam(required = false) String direction){
		
		Pageable pageable =  PageableUtils.createPageable(pageNumber, pageSize, sortBy, direction);
		Page<UserAdminResponse> paginatedUsers =adminService.findUsersByRoleName(role, pageable);
		PagedModel<EntityModel<UserAdminResponse>> pagedUserModel = pagedResourceAssemblerService.toPagedModel(paginatedUsers);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedUserModel);
	}
	
	
	

	@Operation(summary = "Update Role Of User By UserId : ADMIN ONLY")
	@PutMapping("/user/update-role/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> updateRoleByUserId(@PathVariable int id, @RequestBody @Valid RoleRequest roleRequest){
		log.info("this is the roleDto from postman :{}", roleRequest);
		adminService.updateUserRoleById(id, roleRequest);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "User Role Updated Successfully");
	}
	
}//ends controller
