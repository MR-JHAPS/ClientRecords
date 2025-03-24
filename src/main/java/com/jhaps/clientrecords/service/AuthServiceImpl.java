package com.jhaps.clientrecords.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.exception.UserNotFoundException;
import com.jhaps.clientrecords.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authManager;
	private UserRepository userRepo;
	private JWTServiceImpl jwtServiceImpl;
	private UserSecurityService userSecurityService;
	
	public AuthServiceImpl(AuthenticationManager authManager, UserRepository userRepo, JWTServiceImpl jwtServiceImpl,
			UserSecurityService userSecurityService) {
		super();
		this.authManager = authManager;
		this.userRepo = userRepo;
		this.jwtServiceImpl = jwtServiceImpl;
		this.userSecurityService = userSecurityService;
	}

	
	
	/* In verifyUser(UserDto userDto) :
	 * 	 try-Catch block if the authentication is not successful it will catch the exception/error 
	 * 	 and update the wrongPasswordAttempts of that user.
	 * 
	 *	 We cannot use :
	 * 	 'if(!auth.isAuthenticated() ){...}'
	 * 			-- because if(!auth.isAuthenticated()) will never be reached because the exception would be thrown first.
	 * 			   it will be true or exception will be thrown inside springSecurity, it will won't be false so we are using Try-Catch block to watch out for the exception
	 *			   and increment the wrong password attempt.
	 */


	@Override
	public String verifyUser(UserDto userDto) {
		log.info("Action: verify_user_initiated, email: {}", userDto.getEmail());
		User user =  userRepo.findByEmail(userDto.getEmail())
						.orElseThrow(()-> {
							log.error("Error: user_not_found, email: {}", userDto.getEmail());
							throw new UserNotFoundException("User with Email " + userDto.getEmail() + " not found.");
							});
		
		try{
			manageLockedAccount(user); /* All business logic that is related to locked account is handled by this method */
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
			
			Collection<? extends GrantedAuthority> roles = auth.getAuthorities(); /* Getting the user roles/authorities from Authentication auth.*/
			if(roles.isEmpty()) {													/* Checking if the user Role is Empty while logging in.*/
				throw new AccessDeniedException(userDto.getEmail() + "Error: You don't have required role to login");
			}
			log.info("Action: verification_successful,  email: {}", userDto.getEmail());
			userSecurityService.resetLoginAttempts(userDto.getEmail()); // resets the password if log in is successful. 
			return jwtServiceImpl.generateJWTToken(userDto.getEmail());
 		}catch (BadCredentialsException e) {
 			userSecurityService.updateLoginAttempts(userDto);
 			throw new BadCredentialsException( "Error: wrong_Authentication/Credentials_Details, Email : " + userDto.getEmail() );
		}	
	}//ends method
	
	
	
	
	private void manageLockedAccount(User user) {
		log.info("Action: manageLockedAccount_initiated");
		/* Checking if the account is locked.*/
		if(user.isAccountLocked()) { 			 
			/* If the account is locked and it's been 15 minutes
			 *  unlock account and reset the number of attempts to 0.
			 */
			LocalDateTime originalLockedTime = user.getLockTime();
			if(userSecurityService.unlockAfterGivenTime(user)) {
				authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
				log.info(
						" Action: account_unlocked, email: {}, lockedTime: {}, unlockTime: {}",
						user.getEmail(),
						originalLockedTime,
						LocalDateTime.now()
				);
			} else {
				log.warn(
						"Error: account_locked, Remaining_locked_time: {}",
						timeRemaining(user.getLockTime())
				);
				/* If it has not been 15 minutes then account is still locked.*/
				throw new LockedException("Account_Locked"); 
			}
		}
	}
	
	
	/* To calculate the remaining time for the account to be unlocked. */
	private Duration timeRemaining(LocalDateTime lockedTime) {
		LocalDateTime unlockTime = lockedTime.plusMinutes(15);
		Duration remainingLockedTime = Duration.between(unlockTime, LocalDateTime.now());
		return remainingLockedTime;
	}
	
	
	
	
}//ends class
