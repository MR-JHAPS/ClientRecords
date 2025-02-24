package com.jhaps.clientrecords.service;

import java.util.List;
import java.util.Optional;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.User;

public interface UserService {

	public List<UserDto> findAllUsers();
	
	public Optional<UserDto> findUserById(int id);
	
	public Optional<UserDto> findUserByEmail(String email);
	
	public List<UserDto> findUsersByRoleName(String roleName);
	
	public void saveUser(UserDto userDto);
	
	public void deleteUserById(int id);
	
	public void updateUserById(int id);

	public String verifyUser(UserDto userDto);
	
	
	
	
	
}
