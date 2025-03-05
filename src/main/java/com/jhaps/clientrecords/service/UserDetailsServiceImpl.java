package com.jhaps.clientrecords.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.entity.CustomUserDetails;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
//		System.out.println(username + " ---->  before return ");
		
		User user = userRepo.findByEmail(username)
				.orElseThrow(()-> new UsernameNotFoundException("Sorry username " + username +" not found in the Database"));
		return new CustomUserDetails(user);
		
		
		
	}//ends method

	
}//ends class

//		return userRepo.findByEmail(username)
//				.map(userObj->
//						org.springframework.security.core.userdetails.User.builder()
//						.username(userObj.getEmail())
//						.password(userObj.getPassword())
//						.authorities(userObj.getRole().getName())
//						.build()
//					)
//				.orElseThrow(()-> new UsernameNotFoundException("username/email "+ username+" not found"));
		
		
