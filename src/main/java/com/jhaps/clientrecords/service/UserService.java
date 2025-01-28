package com.jhaps.clientrecords.service;

import java.util.List;
import java.util.Optional;

import com.jhaps.clientrecords.entity.User;

public interface UserService {

	public List<User> findAllUsers();
	
	public Optional<User> findUserById(int id);
	
	public Optional<User> findUserByEmail(String email);
	
	public List<User> findUsersByRoleName(String roleName);
	
	public void saveUser(User user);
	
	public void deleteUserById(int id);
	
	public void updateUserById(int id);
	
	
	
	
	
}
