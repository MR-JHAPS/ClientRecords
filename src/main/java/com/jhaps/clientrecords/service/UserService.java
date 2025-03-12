package com.jhaps.clientrecords.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.User;

public interface UserService {

	public String verifyUser(UserDto userDto);
	
	public Page<UserDto> findAllUsers(Pageable pageable);
	
	public UserDto findUserDtoById(int id);
	
	public User findUserById(int id);
	
	public UserDto findUserByEmail(String email);
	
	public Page<UserDto> findUsersByRoleName(String roleName, Pageable pageable);
	
	public void saveUser(UserDto userDto);
	
	public void deleteUserById(int id);
	
	public void updateUserById(int id, UserDto userUpdateInfo);

	
//	public Page<User> ValidateUserListNotEmpty(Page<User> userList);
	
	public boolean isRoleValid(String roleName);
	
	
	
	
	
}
