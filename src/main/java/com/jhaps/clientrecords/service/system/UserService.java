package com.jhaps.clientrecords.service.system;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import com.jhaps.clientrecords.dto.request.user.AdminUpdate;
import com.jhaps.clientrecords.dto.request.user.UserRegister;
import com.jhaps.clientrecords.dto.request.user.UserUpdate;
import com.jhaps.clientrecords.dto.response.RoleDto;
import com.jhaps.clientrecords.dto.response.UserDto;
import com.jhaps.clientrecords.dto.response.user.UserAdmin;
import com.jhaps.clientrecords.dto.response.user.UserGeneralDto;
import com.jhaps.clientrecords.entity.system.User;

public interface UserService {

//	public String verifyUser(UserDto userDto);
	
//	public Page<UserDto> findAllUsers(Pageable pageable);
	
	public User findUserById(int id); /* Return type is "User" | It is used for internal business logic --( Service classes )--*/ 
	
	public UserGeneralDto findUserDtoByEmail(String email); /* Returns "UserGeneralDto" this is for User-dashboard.*/
	
//	public UserAdminDto findUserWithRolesById(int id); /* Returns "UserAdminDto" this contains userRoles to view in admin-Dashboard*/
	
	public User findUserByEmail(String email);
	
//	public UserDto findUserDtoByEmail(String email);
	
//	public Page<UserDto> findUsersByRoleName(String roleName, Pageable pageable);
	
	public void saveUser(User user); /* No Logic just saving the user that is ready. */
	
	public void saveNewUser(UserRegister userRegister); /* Saving/registering new user with logic. */
	
	public void deleteUserByEmail(String email) ;
	
//	public void updateAdmin(String currentUserEmail,AdminUpdate adminUpdate); /* Updates the Admin acccount */
	
	public void updateUserByEmail(String email, UserUpdate userUpdateInfo);
	
//	public void updateUserRoleById(int id, RoleDto roleDto); /* We have the "id" of user and the "RoleDto" that contains the name of the roles.*/
	
	
	
	
	
	
}
