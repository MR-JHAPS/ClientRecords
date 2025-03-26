package com.jhaps.clientrecords.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import com.jhaps.clientrecords.dto.RoleDto;
import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.User;

public interface UserService {

//	public String verifyUser(UserDto userDto);
	
	public Page<UserDto> findAllUsers(Pageable pageable);
	
	public User findUserById(int id); /* Return type is "User" | It is used for internal business logic --( Service classes )--*/ 
	
	public UserDto findUserDtoById(int id); /* Returns "UserDto" specially used for Controller.*/
	
	public User findUserByEmail(String email);
	
	public UserDto findUserDtoByEmail(String email);
	
	public Page<UserDto> findUsersByRoleName(String roleName, Pageable pageable);
	
	public void saveUser(User user); /* No Logic just saving the user that is ready. */
	
	public void saveNewUser(UserDto userDto); /* Saving/registering new user with logic. */
	
	public void deleteUserById(int id) throws AccessDeniedException;
	
	public void updateUserById(int id, UserDto userUpdateInfo);
	
	public void updateUserRoleById(int id, RoleDto roleDto); /* We have the "id" of user and the "RoleDto" that contains the name of the roles.*/
	
//	public void updateLoginAttempts(UserDto userDto);
//	
//	public void unlockAccount(int id);	/* ADMIN - Unlock the locked User Account by ID */
//	
//	public void lockAccount(int id);	/* ADMIN - Lock the user Account by ID*/
//	
//	public void resetLoginAttempts(LoginAttempts loginAttempts);
//	
//	public boolean unlockAfterGivenTime(User user);
//	
	
	
	
	
}
