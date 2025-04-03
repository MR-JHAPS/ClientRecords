package com.jhaps.clientrecords.serviceImpl.system;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.security.model.CustomUserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private UserRepository userRepo;

	public UserDetailsServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username)
				.orElseThrow(()-> new UsernameNotFoundException("Sorry username " + username +" not found in the Database"));
		return new CustomUserDetails(user);
	}//ends method

	
	
}//ends class



