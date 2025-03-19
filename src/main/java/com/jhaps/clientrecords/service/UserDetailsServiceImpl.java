package com.jhaps.clientrecords.service;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.entity.CustomUserDetails;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepo;
	private UserService userService;
	
	public UserDetailsServiceImpl(UserRepository userRepo, UserService userService) {
		this.userRepo = userRepo;
		this.userService = userService;
	}
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username)
				.orElseThrow(()-> new UsernameNotFoundException("Sorry username " + username +" not found in the Database"));
		
		
		//checking if the account is locked.
		if(user.isAccountLocked()) {
			//if the account is locked and it's been 15 minutes unlock account.
			if(userService.unlockAfterGivenTime(user)) {
				return new CustomUserDetails(user);
			} else {
				//if it has not been 15 minutes then still locked.
				throw new LockedException("You account with email : " + username + " is locked");
			}
		}
		return new CustomUserDetails(user);

	}//ends method

	
}//ends class







//without CusomUserDetails Class.

//		return userRepo.findByEmail(username)
//				.map(userObj->
//						org.springframework.security.core.userdetails.User.builder()
//						.username(userObj.getEmail())
//						.password(userObj.getPassword())
//						.authorities(userObj.getRole().getName())
//						.build()
//					)
//				.orElseThrow(()-> new UsernameNotFoundException("username/email "+ username+" not found"));
		
		
