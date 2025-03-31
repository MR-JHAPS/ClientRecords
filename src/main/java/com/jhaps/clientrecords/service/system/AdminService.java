package com.jhaps.clientrecords.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.request.RoleRequest;
import com.jhaps.clientrecords.dto.request.user.AdminUpdate;
import com.jhaps.clientrecords.dto.response.RoleDto;
import com.jhaps.clientrecords.dto.response.UserDto;
import com.jhaps.clientrecords.dto.response.user.UserAdmin;

public interface AdminService {

	public Page<UserAdmin> findAllUsers(Pageable pageable); /* Returns the users with roles and all details so that admin can view in dashboard*/
	
	public UserAdmin findUserWithRolesById(int id); /* Returns user with role for admin to view. */
	
	public Page<UserAdmin> findUsersByRoleName(String roleName, Pageable pageable);
	
	public void updateAdmin(String currentUserEmail,AdminUpdate adminUpdate); /* Updates the Admin acccount */
	
	public void updateUserRoleById(int id, RoleRequest roleRequest); /* We have the "id" of user and the "RoleDto" that contains the name of the roles.*/
	
	public void deleteUserById(int id); /* Admin can delete any user By id */
	
	
	
	
	
	
	
}
