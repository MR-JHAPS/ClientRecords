package com.jhaps.clientrecords.security.customAuth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.jhaps.clientrecords.dto.response.LoginResponse;
import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.exception.system.CustomBadCredentialsException;
import com.jhaps.clientrecords.exception.system.UnauthorizedCustomException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.security.jwt.JWTFilter;
import com.jhaps.clientrecords.security.jwt.JWTService;
import com.jhaps.clientrecords.security.jwt.JWTServiceImpl;
import com.jhaps.clientrecords.security.model.CustomUserDetails;
import com.jhaps.clientrecords.util.mapper.RoleMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
	private JWTService jwtService;
	private UserSecurityService userSecurityService;
	private RoleMapper roleMapper;
	
	
	
/*------------------------------------CONTROLLER LOGIN REQUEST --------------------------------------------------------------------*/
	@Override
	public LoginResponse handleLoginRequest(UserAuthRequest userAuthRequest) {
		log.info("Action: Login verify_user_initiated, for user email: {}", userAuthRequest.getEmail());
		validateLoginCredentials(userAuthRequest);
		User user = getUserByEmail(userAuthRequest.getEmail());
			/**
			 * If the account is locked handles it gracefully.
			 * Contains the business logic for accounts that are locked.
			 */
			manageLockedAccount(user);
			LoginResponse loginResponse = generateLoginResponse(user);
			log.info("Action: Login verification_successful,  email: {}", userAuthRequest.getEmail());
			return loginResponse;
	}//ends method
	
	
/*------------------------------------TOKEN VALIDATION--------------------------------------------------------------------*/
	/**
	 * This method is used to validate the current token. 
	 * Checks if the token is tampered or expired.
	 * @returns : "boolean" status of the token.
	 */
	@Override
	public boolean validateToken(String token, UserDetails userDetails) {
		log.info("Proceeding the validation. Inside ValidateTokenService.");
		boolean isTokenValid = jwtService.validateToken(token, userDetails);
		if(!isTokenValid) {
			throw new AccessDeniedException("Your Token is not valid");
		}
		return true;
	}
	
/*-----------------------------------------------PRIVATE METHOD--------------------------------------------------------------*/
	/**
	 * @return : JWT-Token, refreshToken as String and isEmailVerified as Boolean.
	 *	 We cannot use :
	 * 	 'if(!auth.isAuthenticated() ){...}'
	 * 			-- because if(!auth.isAuthenticated()) will never be reached because the exception would be thrown first.
	 * 			   it will be true or exception will be thrown inside springSecurity, it will won't be false so we are using Try-Catch block to watch out for the exception
	 *			   and increment the wrong password attempt.
	 */
	private void validateLoginCredentials(UserAuthRequest authRequest) {
		int totalPossibleAttempts = 3;
		try {
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
			CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
			User user = userDetails.getUser();
			log.info("before resetting login Attempts");
			userSecurityService.resetLoginAttempts(user);  
			log.info("loginAttempts resetted");
		}catch (BadCredentialsException e) {
 			int userAttempts = userSecurityService.updateLoginAttempts(authRequest.getEmail());
 			log.info("User Attempts received in verifyLoginUser from updateLoginAttempts: {}", userAttempts);
 			int remainingAttempts = totalPossibleAttempts - userAttempts;
 			throw new CustomBadCredentialsException( "Custom Exception : wrong_Authentication/Credentials_Details, Email : " + authRequest.getEmail(),
 														remainingAttempts );
		}	
	}
	
	
	
	/**
	 *This is the response that carries if the userEmail is Registered or not
	 *@returns : LoginResponse type.
	 */
	private LoginResponse generateLoginResponse(User user) {
		Set<String> roleSet = getUserRoles(user);
		boolean isEmailVerified = user.isEmailVerified();
		String jwtToken = jwtService.generateJWTToken(user.getEmail(), roleSet ); //15 min valid.
		
		//FOR DEBUGGING
//		List<String> tokenRoleList = jwtService.extractClaim(jwtToken, claims -> claims.get("role", List.class));
//		Set<String> tokenRole = new HashSet<>(tokenRoleList);
//		log.info("THis is the role obtained from the token: {}", tokenRole);
		
		String refreshToken = jwtService.generateRefreshToken(user.getEmail(), roleSet); // 12 hours valid
		LoginResponse loginResponse = new LoginResponse();
										loginResponse.setToken(jwtToken);
										loginResponse.setRefreshToken(refreshToken);
										loginResponse.setEmailVerified(isEmailVerified);
		return loginResponse;
	}
	
	
	
	
	
	/** Handles all the action of Locked Accounts. */
	private void manageLockedAccount(User user) {
		log.info("Action: manageLockedAccount_initiated");
		/* Checking if the account is locked.*/
		if(user.isAccountLocked()) { 			 
			LocalDateTime originalLockedTime = user.getLockTime();
			/* If the account is locked and it's been 15 minutes
			 *  unlock account and reset the number of attempts to 0.
			 */
			if(userSecurityService.unlockAfterGivenTime(user)) {
				authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
				log.info(" Action: account_unlocked, email: {}, lockedTime: {}, unlockTime: {}",
							user.getEmail(), originalLockedTime, LocalDateTime.now() );
			} else {
				log.warn("Error: account_locked, Remaining_locked_time: {}",
							timeRemaining(user.getLockTime()) );
				/* If it has not been 15 minutes then account is still locked.*/
				throw new LockedException("Account_Locked"); 
			}
		}//ends-if
	}//ends method
	
	
	
	
	/* To calculate the remaining time for the account to be unlocked. */
	private Duration timeRemaining(LocalDateTime lockedTime) {
		LocalDateTime unlockTime = lockedTime.plusMinutes(15);
		Duration remainingLockedTime = Duration.between(unlockTime, LocalDateTime.now());
		return remainingLockedTime;
	}


	/* Converting the roles to Set of String*/
	private Set<String> getUserRoles(User user){
		String userEmail = user.getEmail();
		Set<Role> roles = user.getRoles();
		log.info("These are the roles obtained from user.getRoles() : {} of type set<Role>",roles);
		/* Checking if the user Role is Empty while logging in.*/
		if(roles.isEmpty()) {												
			throw new AccessDeniedException(userEmail + "Error: You don't have required role to login");
		}
		Set<String> roleSet = roleMapper.roleToStringSet(roles);
		log.info("THis is the role of user After mapper to Set<String>: {}",roleSet );
		return roleSet;
	}
	
	
	
	
	private User getUserByEmail( String email) {
		User user =  userRepo.findByEmail(email)
				.orElseThrow(()-> {
					log.error("Error: user_not_found, email: {}", email);
					throw new UserNotFoundException("User with Email " + email + " not found.");
					});
		return user;
	}
	
	


		
		

	
	
	
	
	
}//ends class
