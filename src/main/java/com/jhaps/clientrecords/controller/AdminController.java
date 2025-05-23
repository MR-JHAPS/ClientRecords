package com.jhaps.clientrecords.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.jhaps.clientrecords.dto.response.user.UserGeneralResponse;
import com.jhaps.clientrecords.entity.system.Image;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.security.model.CustomUserDetails;
import com.jhaps.clientrecords.service.system.AdminService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.PageableUtils;
import com.jhaps.clientrecords.util.mapper.UserMapper;

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
	private UserMapper userMapper;
	
	public AdminController(ApiResponseBuilder apiResponseBuilder, AdminService adminService,
			PagedResourceAssemblerService<UserAdminResponse> pagedResourceAssemblerService,
			UserMapper userMapper) {
		this.apiResponseBuilder = apiResponseBuilder;
		this.pagedResourceAssemblerService = pagedResourceAssemblerService;
		this.adminService = adminService;
		this.userMapper = userMapper;
	}
	
	
	
	@Operation(summary = "Get authenticated admin's details")
	@GetMapping("/me")
	public ResponseEntity<ApiResponseModel<UserAdminResponse>> getUserSelf(@AuthenticationPrincipal CustomUserDetails userDetails) {
		int userId = userDetails.getUser().getId();
		User user = adminService.getCurrentAdmin(userId);
		UserAdminResponse userAdminResponse = this.userMapper.toUserAdminResponse(user);

	    // Use image URL if present, else use default image
	    String imagePath = user.getProfileImage()
	            .map(Image::getUrl)
	            .orElse("defaultImage.png");
	    userAdminResponse.setProfileImageUrl(imagePath);
	    return apiResponseBuilder.buildApiResponse(
	            ResponseMessage.SUCCESS,
	            HttpStatus.OK,
	            userAdminResponse
	    );
	}
	
	
	@Operation(summary = "Get List Of All The Users (Paginated)")
	@GetMapping("/users")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<UserAdminResponse>>>> getAllUsers(
								@RequestParam(defaultValue="0") int page,
								@RequestParam(defaultValue="10") int size,
								@RequestParam(required = false) String sortBy,
								@RequestParam(required = false) String direction
								){
		Pageable pageable =  PageableUtils.createPageable(page, size, sortBy, direction);
		Page<User> paginatedUsers = adminService.findAllUsers(pageable);
		/* Mapping : Page<User> to Page<UserAdminResponse> */
		Page<UserAdminResponse> paginatedResponse = paginatedUsers.map(userMapper::toUserAdminResponse);
		PagedModel<EntityModel<UserAdminResponse>> pagedUserModel = pagedResourceAssemblerService.toPagedModel(paginatedResponse);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedUserModel);
	}
	
	
	
	@Operation(summary = "Get User-Details(including their roles) By ID")
	@GetMapping("/users/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<UserAdminResponse>> getUserWithRolesByUserId(@PathVariable int id) {
		User user = adminService.findUserWithRolesById(id);
		/* Mapping : User to UserAdminResponse */
		UserAdminResponse userResponse = userMapper.toUserAdminResponse(user);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userResponse );
	}
	
	

	@Operation(summary = " Delete User By Id ")
	@DeleteMapping("/users/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> deleteUserById(@PathVariable int id){
		adminService.deleteUserById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_DELETED, HttpStatus.NO_CONTENT);
	}
	
	
	
	 //=== Search Endpoints ===//
	@Operation(summary = "Search user by user Email(Unique)", description = "User-Email is unique so it will return only one data.")
	@GetMapping("/users/search")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<UserAdminResponse>> searchUserByEmail(@RequestParam String email) {
		User user = adminService.searchUserByEmail(email);
		/* Mapping : User to UserAdminResponse */
		UserAdminResponse userResponse = userMapper.toUserAdminResponse(user);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, userResponse);
	}

	
	
	@Operation(summary = "Get List Of Users By Role Name (Paginated)")
	@GetMapping("/users/by-role")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<UserAdminResponse>>>> getUsersByRole(
							@NotBlank @RequestParam String role,
							@RequestParam(defaultValue="0") int page,
							@RequestParam(defaultValue="10") int size,
							@RequestParam(required = false) String sortBy,
							@RequestParam(required = false) String direction){
		
		Pageable pageable =  PageableUtils.createPageable(page, size, sortBy, direction);
		Page<User> paginatedUsers = adminService.searchUsersByRoleName(role, pageable);
		/* Mapping : Page<User> to Page<UserAdminResponse> */
		Page<UserAdminResponse> paginatedResponse = paginatedUsers.map(userMapper::toUserAdminResponse);
		PagedModel<EntityModel<UserAdminResponse>> pagedUserModel = pagedResourceAssemblerService.toPagedModel(paginatedResponse);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedUserModel);
	}
	
	
	
	@Operation(summary = "Update Role Of Users.")
	@PutMapping("/users/{id}/roles")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> updateRoleByUserId(@PathVariable int id, @RequestBody @Valid RoleRequest roleRequest){
		log.info("this is the roleDto from postman :{}", roleRequest);
		adminService.updateUserRoleById(id, roleRequest);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "User Role Updated Successfully");
	}
	
	
	
	@Operation(summary = " Update Admin's own profile")
	@PutMapping("/me")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<ApiResponseModel<String>> updateAdmin(@RequestBody AdminUpdateRequest request,
											@AuthenticationPrincipal CustomUserDetails userDetails){
		int userId = userDetails.getUser().getId();
		adminService.updateCurrentAdmin(userId, request);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.ADMIN_UPDATED, HttpStatus.OK);
	}

	
	
	
	
}//ends controller
