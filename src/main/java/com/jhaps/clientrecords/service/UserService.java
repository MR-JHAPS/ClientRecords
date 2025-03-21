package com.jhaps.clientrecords.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import com.jhaps.clientrecords.dto.LoginAttempts;
import com.jhaps.clientrecords.dto.RoleDto;
import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.User;

public interface UserService {

	public String verifyUser(UserDto userDto);
	
	public Page<UserDto> findAllUsers(Pageable pageable);
	
	public UserDto findUserDtoById(int id); //this is for controller and public uses
	
	public UserDto findUserByEmail(String email);
	
	public Page<UserDto> findUsersByRoleName(String roleName, Pageable pageable);
	
	public void saveUser(UserDto userDto);
	
	public void deleteUserById(int id) throws AccessDeniedException;
	
	public void updateUserById(int id, UserDto userUpdateInfo);
	
	public void updateUserRoleById(int id, RoleDto roleDto); //we have the id of user and the roleDto that contains the name of the roles.
	
//	public void updateLoginAttempts(LoginAttempts loginAttempts);
	
	public void resetLoginAttempts(LoginAttempts loginAttempts);
	
//	public boolean unlockAfterGivenTime(User user);
	
	
	
	
	
}
