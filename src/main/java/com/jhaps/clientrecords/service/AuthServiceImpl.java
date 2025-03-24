package com.jhaps.clientrecords.service;

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
		User user =  userRepo.findByEmail(userDto.getEmail())
						.orElseThrow(()-> new UserNotFoundException("User with Email " + userDto.getEmail() + " not found."));
		
		/* Checking if the account is locked.*/
		if(user.isAccountLocked()) { 			 
			/* If the account is locked and it's been 15 minutes
			 *  unlock account and reset the number of attempts to 0.
			 */
			if(userSecurityService.unlockAfterGivenTime(user)) {
				authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
				log.info("I am inside the AuthService-> verifyUser(), Your account is unlocked now, it's been 15 minutes since your last try. ");
			} else {
				throw new LockedException("AuthService---> verifyUser() ---> Your account with email : " + userDto.getEmail() + " is locked"); /* If it has not been 15 minutes then account is still locked.*/
			}
		}
		
		/* Validating User Credentials. */
		try{
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
			
			Collection<? extends GrantedAuthority> roles = auth.getAuthorities(); /* Getting the user roles/authorities from Authentication auth.*/
			if(roles.isEmpty()) {													/* Checking if the user Role is Empty while logging in.*/
				throw new AccessDeniedException(userDto.getEmail() + " You don't have required role to login");
			}
			log.info("User Verified in userServiceImpl for email: {}", userDto.getEmail());
			userSecurityService.resetLoginAttempts(userDto.getEmail()); // resets the password if log in is successful. 
			return jwtServiceImpl.generateJWTToken(userDto.getEmail());
 		}catch (BadCredentialsException e) {
 			userSecurityService.updateLoginAttempts(userDto);
 			throw new BadCredentialsException( "Wrong Authentication/Credentials Details for Email : " + userDto.getEmail() );
		}	
	}//ends method
	
	
	
	
}//ends class
