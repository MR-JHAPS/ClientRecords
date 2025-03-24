package com.jhaps.clientrecords.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.CustomUserDetails;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.exception.UserNotFoundException;
import com.jhaps.clientrecords.repository.UserRepository;
import com.jhaps.clientrecords.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JWTServiceImpl jwtServiceImpl;
	
	@Autowired
	private UserService userService;
	
	
	
	
	/* In try-Catch block if the authentication is not successful it will catch the exception/error 
	 * and update the wrongPasswordAttempts of that user.
	 * 
	 * We cannot use :
	 * 	 'if(!auth.isAuthenticated() ){...}'
	 * 			-- because if(!auth.isAuthenticated()) will never be reached because the exception would be thrown first.
	 * 			   it will be true or exception will be thrown inside springSecurity, it will won't be false so we are using Try-Catch block to watch out for the exception
* 					   and increment the wrong password attempt.
	 *  */
	@Override
	public String verifyUser(UserDto userDto) {
		User user =  userRepo.findByEmail(userDto.getEmail())
						.orElseThrow(()-> new UserNotFoundException("User with Email " + userDto.getEmail() + " not found."));
		
		/* Checking if the account is locked.*/
		if(user.isAccountLocked()) { 			 
			/* If the account is locked and it's been 15 minutes
			 *  unlock account and reset the number of attempts to 0.
			 */
			if(userService.unlockAfterGivenTime(user)) {
				authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
			} else {
				throw new LockedException("You account with email : " + userDto.getEmail() + " is locked"); /* If it has not been 15 minutes then account is still locked.*/
			}
		}
		
		
		
		try{
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
			
			Collection<? extends GrantedAuthority> roles = auth.getAuthorities(); /* Getting the user roles/authorities from Authentication auth.*/
			if(roles.isEmpty()) {													/* Checking if the user Role is Empty while logging in.*/
				throw new AccessDeniedException(userDto.getEmail() + " You don't have required role to login");
			}
			log.info("User Verified in userServiceImpl for email: {}", userDto.getEmail());
			return jwtServiceImpl.generateJWTToken(userDto.getEmail());
 		}catch (BadCredentialsException e) {
 			userService.updateLoginAttempts(userDto);
 			throw new BadCredentialsException( "Wrong Authentication/Credentials Details for Email : " + userDto.getEmail() );
		}	
	}//ends method
	
	
	
	
}//ends class
