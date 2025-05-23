package com.jhaps.clientrecords.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.request.RoleRequest;
import com.jhaps.clientrecords.dto.request.user.AdminUpdateRequest;
import com.jhaps.clientrecords.dto.response.user.UserAdminResponse;
import com.jhaps.clientrecords.entity.system.User;

public interface AdminService {
	
	
	
	Page<User> findAllUsers(Pageable pageable); /* Returns the users with roles and all details so that admin can view in dashboard*/
	
	User findUserWithRolesById(int id); /* Returns user with role for admin to view. */
	
	Page<User> searchUsersByRoleName(String roleName, Pageable pageable);
	
	void updateCurrentAdmin(int userId, AdminUpdateRequest adminUpdateRequest); /* Updates the Admin acccount */
	
	void updateUserRoleById(int id, RoleRequest roleRequest); /* We have the "id" of user and the "RoleDto" that contains the name of the roles.*/
	
	void deleteUserById(int id); /* Admin can delete any user By id */
	
	User searchUserByEmail(String email);
	
	User getCurrentAdmin(int userId);


	
	
	
	
	
}
