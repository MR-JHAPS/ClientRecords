package com.jhaps.clientrecords.security.customAuth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * This class validates :
 * 			 "rawProvidedPassword" with "encodedSavedDbPassword". // verifying the user
 * 			 "Password" with "confirmPassword". // confirms these raw password are equal.
 * 
 * 
 * */



@Component
@AllArgsConstructor
@Slf4j
public class PasswordValidator {
	
	private PasswordEncoder passwordEncoder;
	
	
	
	/*
	 * @param password is your entered password
	 * @param confirmPassword is to confirm your password
	 * 		password and 
	 * */
	public void validatePasswordMatch(String password, String confirmPassword) {
		if(password==null || confirmPassword==null) {
			log.warn("Password Field cannot be null.");
			throw new IllegalArgumentException("Password Field Cannot be null");
		}
		
		if(!password.equals(confirmPassword)) {
			log.warn("Password and Confirm-Password mismatch. ");
			throw new IllegalArgumentException(" Password and Confirm-Password mismatch" );
		}
	}
	
	
	/*
	 * This methods checks if the raw password given by user matches the encoded password saved in Database.
	 * @param : currentRawPassword is raw password given by user for verification.
	 * @param : passwordInDb is the encoded password saved in userDatabase.
	 * 
	 * */
	public void verifyCurrentPassword(String currentRawPassword, String passwordInDb) {
		
		if (currentRawPassword == null || passwordInDb == null) {
			log.warn("Password verification failed | currentRawPassword or passwordInDb is null.");
	        throw new BadCredentialsException("Password verification failed");
	    }
		
		if(!passwordEncoder.matches(currentRawPassword, passwordInDb)) {
			log.warn("Your current Password Does not matches your account password.");
			throw new BadCredentialsException(" Your current Password Does not matches your account password");
		}
	}
	
	
	
	
	
	
}
