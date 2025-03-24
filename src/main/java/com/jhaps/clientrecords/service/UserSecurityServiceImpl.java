package com.jhaps.clientrecords.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.LoginAttempts;
import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.exception.UserNotFoundException;
import com.jhaps.clientrecords.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class UserSecurityServiceImpl implements UserSecurityService{

	private UserService userService;
	private UserRepository userRepo;

	public UserSecurityServiceImpl(UserService userService, UserRepository userRepo) {
		super();
		this.userService = userService;
		this.userRepo = userRepo;
	}
	


	/*	THIS IS TO UPDATE THE USER LOGIN ATTEMPTS AFTER EACH WRONG PASSWORD/CREDENTIALS. */
	@Override
	public void updateLoginAttempts(UserDto userDto) {
		String email = userDto.getEmail(); //email of user who made wrong password attempt
		User user = userRepo.findByEmail(email)
					.orElseThrow(()-> new UserNotFoundException("Error: User_Not_Found, Email : " + email));
		int previousAttempts = user.getAttempts(); /* Getting the user wrong password attempts from the Database if exists */
		int currentAttempts = previousAttempts + 1 ;  /* current wrong password attempts*/
		user.setAttempts(currentAttempts);		/* Saving the current wrong password attempts in the user Database.*/
		if(currentAttempts >= 3) {				/* If the wrong attempts equals 3 or more than 3 attempts the account will be locked along with TimeStamp*/
			user.setAccountLocked(true); 
			user.setLockTime(LocalDateTime.now());
			log.debug("Warning: You made 3 wrong attempts ");
		}
		userService.saveUser(user);
	}
	
	
	
	/* 	Unlocks the Locked User Automatically after 15 minutes. */
	@Override
	public boolean unlockAfterGivenTime(User user) {
		if(user.isAccountLocked() && user.getLockTime()!=null) {
			LocalDateTime unlockTime = user.getLockTime().plusMinutes(15);
			if(LocalDateTime.now().isAfter(unlockTime)) {  // if current time is 15 minutes after user.getLockTime
				user.setAttempts(0);
				user.setAccountLocked(false);
				user.setLockTime(null);
				userRepo.save(user);
				return true;
			}
		}
		return false;	
	}
	
	
	
	
	@Override
	public void resetLoginAttempts(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(()-> new UserNotFoundException("Error:User_Not_Found, Email : " + email));
		user.setAttempts(0);
		userService.saveUser(user);
	}



	/* Unlock the locked User Account */
	@Override
	public void unlockAccount(int id) {
		User user = userService.findUserById(id);
		user.setAccountLocked(false);
		userService.saveUser(user);
	}



	/* Lock the User Account */
	@Override
	public void lockAccount(int id) {
		User user = userService.findUserById(id);
		user.setAccountLocked(true);
		userService.saveUser(user);
	}

	
	
	
	
}// ends class
