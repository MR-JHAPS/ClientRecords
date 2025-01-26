package com.jhaps.clientrecords.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleServiceImpl roleServiceImpl;

	@Override
	public Optional<User> findUserByEmail(String email) {
		return userRepo.findByEmail(email);	
	}

	@Override
	public List<User> findUsersByRoleName(String roleName) {
		return userRepo.findByRole_Name(roleName);
	}

	@Override
	public void saveUser(User user) {
		
		String userEmail = user.getEmail();
		userRepo.findByEmail(userEmail)
							.ifPresentOrElse(
									existingUser-> 
										System.out.println("User "+userEmail+" already exists"),
									()->{
										userRepo.save(user);
										System.out.println("User created successfully");
									}
							);
	}//ends method

	
	@Override
	public void deleteUserById(int id) {
		
		if(userRepo.existsById(id)) {
			userRepo.deleteById(id);
		}else {
			throw new UsernameNotFoundException("user with "+id + " not found");
		}
	}//ends method.

	
	@Override
	public void updateUserById(int id) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	
}//ends class
