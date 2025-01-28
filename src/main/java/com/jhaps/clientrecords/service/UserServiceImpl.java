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

	private UserRepository userRepo;
	private RoleServiceImpl roleServiceImpl;
	
	public UserServiceImpl(UserRepository userRepo, RoleServiceImpl roleServiceImpl) {
		this.userRepo = userRepo;
		this.roleServiceImpl = roleServiceImpl;
	}
	
	
	
	@Override
	public List<User> findAllUsers() {
		return userRepo.findAll();
	}
	
	@Override
	public Optional<User> findUserById(int id) {
		return userRepo.findById(id);				
	}

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
		if(!userRepo.existsById(id)) {
			throw new UsernameNotFoundException("user with "+id + " not found");
		}
		userRepo.deleteById(id);
	}//ends method.

	
	@Override
	public void updateUserById(int id) {
		// TODO Auto-generated method stub
		
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
}//ends class
