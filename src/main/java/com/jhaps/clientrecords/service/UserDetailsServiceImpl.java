package com.jhaps.clientrecords.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private UserRepository userRepo;

	
	public UserDetailsServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}




	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username)
				.orElseThrow(()-> new UsernameNotFoundException("Sorry username " + username +" not found in the Database"));

//		if(user.isAccountLocked()) { /* checking if the account is locked.*/
//			if(unlockAfterGivenTime(user)) { 	/* If the account is locked and it's been 15 minutes unlock account.*/
//				return new CustomUserDetails(user);
//			} else {
//				throw new LockedException("You account with email : " + username + " is locked"); /* If it has not been 15 minutes then account is still locked.*/
//			}
//		}
		return new CustomUserDetails(user);

	}//ends method

	
	
//	/* This method is to unlock the account after 15 minutes */
//	private boolean unlockAfterGivenTime(User user) {
//		if(user.isAccountLocked() && user.getLockTime()!=null) {
//			LocalDateTime unlockTime = user.getLockTime().plusMinutes(15);
//			if(LocalDateTime.now().isAfter(unlockTime)) {  // if current time is 15 minutes after user.getLockTime
//				user.setAttempts(0);
//				user.setAccountLocked(false);
//				user.setLockTime(null);
//				userRepo.save(user);
//				return true;
//			}
//		}
//		return false;	
//	}
	
	
	
}//ends class



