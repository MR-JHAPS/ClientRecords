package com.jhaps.clientrecords.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.request.RoleRequest;
import com.jhaps.clientrecords.dto.request.user.AdminUpdateRequest;
import com.jhaps.clientrecords.dto.response.user.UserAdminResponse;

public interface AdminService {

	public Page<UserAdminResponse> findAllUsers(Pageable pageable); /* Returns the users with roles and all details so that admin can view in dashboard*/
	
	public UserAdminResponse findUserWithRolesById(int id); /* Returns user with role for admin to view. */
	
	public Page<UserAdminResponse> findUsersByRoleName(String roleName, Pageable pageable);
	
	public void updateAdmin(String currentUserEmail,AdminUpdateRequest adminUpdateRequest); /* Updates the Admin acccount */
	
	public void updateUserRoleById(int id, RoleRequest roleRequest); /* We have the "id" of user and the "RoleDto" that contains the name of the roles.*/
	
	public void deleteUserById(int id); /* Admin can delete any user By id */
	
	
	
	
	
	
	
}
