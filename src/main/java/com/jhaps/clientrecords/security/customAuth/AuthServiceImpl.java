package com.jhaps.clientrecords.security.customAuth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.jhaps.clientrecords.dto.request.user.UserAuthRequest;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.exception.system.UnauthorizedCustomException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.security.jwt.JWTFilter;
import com.jhaps.clientrecords.security.jwt.JWTServiceImpl;
import com.jhaps.clientrecords.util.mapper.RoleMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/*
 * This is called from public controller ("/login")
 * It checks if the password is incorrect if so it will increase the number
 * of wrong attempts of that user in the Database.
 * Also, after 3 times it locks the account and unlocks after 15 minutes
 * 
 * This handles the loginAuthentication.
 * 
 * */




@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authManager;
	private UserRepository userRepo;
	private JWTServiceImpl jwtServiceImpl;
	private UserSecurityService userSecurityService;
	private JWTFilter jwtFilter;
	private RoleMapper roleMapper;
	

	
	
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
	public String verifyUser(UserAuthRequest userAuthRequest) {
		log.info("Action: verify_user_initiated, email: {}", userAuthRequest.getEmail());
		User user =  userRepo.findByEmail(userAuthRequest.getEmail())
						.orElseThrow(()-> {
							log.error("Error: user_not_found, email: {}", userAuthRequest.getEmail());
							throw new UserNotFoundException("User with Email " + userAuthRequest.getEmail() + " not found.");
							});
		
		try{
			manageLockedAccount(user); /* All business logic that is related to locked account is handled by this method */
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthRequest.getEmail(), userAuthRequest.getPassword()));
			
			Collection<? extends GrantedAuthority> roles = auth.getAuthorities(); /* Getting the user roles/authorities from Authentication auth.*/
			if(roles.isEmpty()) {													/* Checking if the user Role is Empty while logging in.*/
				throw new AccessDeniedException(userAuthRequest.getEmail() + "Error: You don't have required role to login");
			}
			log.info("Action: verification_successful,  email: {}", userAuthRequest.getEmail());
			userSecurityService.resetLoginAttempts(userAuthRequest.getEmail()); // resets the password if log in is successful. 
			/* Converting the roles to */
			Set<String> roleSet = roleMapper.roleToStringSet(roles);
			return jwtServiceImpl.generateJWTToken(userAuthRequest.getEmail(), roleSet );
 		}catch (BadCredentialsException e) {
 			userSecurityService.updateLoginAttempts(userAuthRequest);
 			throw new BadCredentialsException( "Error: wrong_Authentication/Credentials_Details, Email : " + userAuthRequest.getEmail() );
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



	
/*------------------------------------LOG OUT USER------------------------------------------------------------------------------------*/
	
	// need to underStand this further.
	
	@Override
	public String logOutUser(String authHeader, HttpServletRequest request, HttpServletResponse response, UserDetails userDetails) {
		
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			throw new UnauthorizedCustomException(" Your auth-Header is either null or doesn't start with 'Bearer '. ");
		}
		
		
		
		String token = authHeader.substring(7);
		
		boolean isTokenExpired = jwtServiceImpl.isTokenExpired(token);
		boolean isTokenValid = jwtServiceImpl.validateToken(token, userDetails);
		
		if(isTokenExpired || !isTokenValid) {
			response.setHeader("Authorization", null);
	
		}
		return null;
	}



	@Override
	public boolean validateToken(String token, UserDetails userDetails) {
		log.info("Proceeding the validation. Inside ValidateTokenService.");
		boolean isTokenValid = jwtServiceImpl.validateToken(token, userDetails);
		
		if(!isTokenValid) {
			throw new AccessDeniedException("Your Token is not valid");
		}
		return true;
	}

		
		

	
	
	
	
	
}//ends class
