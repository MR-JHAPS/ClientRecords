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
import com.jhaps.clientrecords.dto.request.user.AdminUpdate;
import com.jhaps.clientrecords.dto.response.RoleDto;
import com.jhaps.clientrecords.dto.response.user.UserAdmin;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.system.AdminService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.SortBuilder;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private ApiResponseBuilder apiResponseBuilder;	
	private AdminService adminService;
	private PagedResourceAssemblerService<UserAdmin> pagedResourceAssemblerService;
	
	public AdminController(ApiResponseBuilder apiResponseBuilder, AdminService adminService,
			PagedResourceAssemblerService<UserAdmin> pagedResourceAssemblerService) {
		this.apiResponseBuilder = apiResponseBuilder;
		this.pagedResourceAssemblerService = pagedResourceAssemblerService;
		this.adminService = adminService;
	}
	
	
	
	
	@Operation(summary = "Get List Of All The Users")
	@GetMapping("/findAll")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<UserAdmin>>>> getAllUsers(
								@RequestParam(defaultValue="0") int pageNumber,
								@RequestParam(defaultValue="10") int pageSize,
								@RequestParam(required = false) String sortBy,
								@RequestParam(required = false) String direction
								){
		//if "sortBy" and "Direction" parameters is null then "Sort sort" will return null.
		Sort sort = SortBuilder.createSorting(direction, sortBy);
		//if sort returns null, don't apply sorting in PageRequest/pageable.
		Pageable pageable =  (sort == null) ? PageRequest.of(pageNumber, pageSize) : PageRequest.of(pageNumber, pageSize, sort);
		Page<UserAdmin> paginatedUsers = adminService.findAllUsers(pageable);
		PagedModel<EntityModel<UserAdmin>> pagedUserModel = pagedResourceAssemblerService.toPagedModel(paginatedUsers);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedUserModel);
	}
	
	
	@Operation(summary = "Get User along with Roles By ID")
	@GetMapping("/user/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<UserAdmin>> getUserWithRolesById(@PathVariable int id) {
		UserAdmin userAdmin = adminService.findUserWithRolesById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userAdmin);
	}
	
	
	@Operation(summary = " Update Admin info ")
	@DeleteMapping("/update/me")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> updateAdmin(@RequestBody AdminUpdate adminUpdate,
											@AuthenticationPrincipal UserDetails userDetails){
		String email = userDetails.getUsername();
		adminService.updateAdmin(email, adminUpdate);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.ADMIN_UPDATED, HttpStatus.OK);
	}

	
	@Operation(summary = "Get List Of Users By Role Name")
	@GetMapping("/role/{role}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<UserAdmin>>>> getUsersByRole(@PathVariable String role,
												@RequestParam(defaultValue="0") int pageNumber,
												@RequestParam(defaultValue="10") int pageSize,
												@RequestParam(required = false) String sortBy,
												@RequestParam(required = false) String direction){
		
		//if "sortBy" and "Direction" parameters is null then "Sort sort" will return null.
		Sort sort = SortBuilder.createSorting(direction, sortBy);
		//if sort returns null, don't apply sorting in PageRequest/pageable.
		Pageable pageable =  (sort == null) ? PageRequest.of(pageNumber, pageSize) : PageRequest.of(pageNumber, pageSize, sort);
		Page<UserAdmin> paginatedUsers = adminService.findUsersByRoleName(role, pageable);
		PagedModel<EntityModel<UserAdmin>> pagedUserModel = pagedResourceAssemblerService.toPagedModel(paginatedUsers);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedUserModel);
	}
	
	
	
	@Operation(summary = "Update Role Of User By UserId : ADMIN ONLY")
	@PutMapping("/user/update/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> updateRoleByUserId(@PathVariable int id, @RequestBody @Valid RoleRequest roleRequest){
		log.info("this is the roleDto from postman :{}", roleRequest);
		adminService.updateUserRoleById(id, roleRequest);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "User Role Updated Successfully");
	}
	
	
	@Operation(summary = " Delete User By Id ")
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> deleteUserById(@PathVariable int id){
		adminService.deleteUserById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_DELETED, HttpStatus.NO_CONTENT);
	}
	
	
	
	
}//ends controller
