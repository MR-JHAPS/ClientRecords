package com.jhaps.clientrecords.security.jwt;

import java.util.Date;
import java.util.Set;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JWTService {

	
	

	
	
	/*	@return a "String" type JWTToken */
	String generateJWTToken(String email, Set<String> roles);
	
	
	/**
	 * @returns : refreshToken with longer validity.
	 */
	String generateRefreshToken(String email, Set<String> roles);
	
	
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
