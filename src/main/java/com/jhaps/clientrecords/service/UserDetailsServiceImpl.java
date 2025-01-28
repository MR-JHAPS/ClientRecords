package com.jhaps.clientrecords.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepo.findByEmail(username)
				.map(userObj->
						org.springframework.security.core.userdetails.User.builder()
						.username(userObj.getEmail())
						.password(userObj.getPassword())
						.authorities(userObj.getRole().getName())
						.build()
					)
				.orElseThrow(()-> new UsernameNotFoundException("username/email "+ username+" not found"));
		
	}//ends method

	
	/*OR We can create a "CustomUserDetails" class that implements -->"UserDetails" interface
	 *  and link our custom "User" to the "CustomUserDetails" class */
	
	
}//ends class
