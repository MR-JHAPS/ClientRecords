package com.jhaps.clientrecords.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.User;

public interface UserService {

	public Page<UserDto> findAllUsers(Pageable pageable);
	
	public Optional<UserDto> findUserById(int id);
	
	public Optional<UserDto> findUserByEmail(String email);
	
	public Page<UserDto> findUsersByRoleName(String roleName, Pageable pageable);
	
	public void saveUser(UserDto userDto);
	
	public void deleteUserById(int id);
	
	public void updateUserById(int id);

	public String verifyUser(UserDto userDto);
	
	
	
	
	
}
