package com.jhaps.clientrecords.security.spring;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JWTService {

	
	
	/*	@return a "SecretKey" type key generated using "final String value"
	 *  		that is stored in "application.properties".
	 *  
	 *	@use to generate a JWTToken.
	 */
	SecretKey generateKeyForTokenSignature(); 
	
	
	/*	@return a "String" type JWTToken */
	String generateJWTToken(String email);
	
	
	/* Extracts claims from the token */
	<T> T extractClaim(String token , Function<Claims, T> claimResolver);
	
	
	/*	@return "String" type data.
	 *  Extracts the Username from the token.
	 */
	String extractUsername(String token);
	
	
	/*	@return "Date" type data.
	 *  Extracts the expiration date from the token.
	 */
	Date extractExpiration(String token);
	
	
	/* @return "boolean" Checking if token is expired. */
	boolean isTokenExpired(String token);
	
	
	/* @return "boolean" Checking if token is valid. 
	 * @checks if token is not expired and subject/email
	 * 		   in token is valid.
	 */
	boolean validateToken(String token, UserDetails userDetails);
	
	
	
}// ends interface
