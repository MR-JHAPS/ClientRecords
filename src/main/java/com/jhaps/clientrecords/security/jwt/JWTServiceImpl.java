package com.jhaps.clientrecords.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWTServiceImpl {

	/*
	 *  @value Jwt-secret-key injected from application.properties.
	 */
	@Value("${jwt.secret.key}")
	private String secretKey ;
	
	
//------------------------- GENERATING JWT TOKEN ------------------------------------------------------------------------------------------	
		
	//generating key for the signing/signature it will be of type SecretKey .
	private SecretKey generateKeyForTokenSignature() {
		//converting our string to byte because To convert it to secretKey of HmacShaKey.
		byte[] secretKeyByte = Decoders.BASE64.decode(secretKey);
		// SecretKey keyForSignature = Keys.hmacShaKeyFor(secretKeyByte);
		//return keyForSignature;
		return Keys.hmacShaKeyFor(secretKeyByte);
	}
	
	
	public String generateJWTToken(String email, Set<String> roles) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", roles);
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(email)	
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 *60)) // token valid for 60 minutes.
				.and()
				.signWith(generateKeyForTokenSignature())
				.compact();
	}
	
//--------------------------------------- Validating Token ------------------------------------------------------------------------------------------

	//Extracting All the Claims from token
	private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKeyForTokenSignature())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
	
	
	//method that implements the Functional Interface and allows the extraction of "User-name", "expiration-date", "issue date".
	public <R> R extractClaim(String token , Function<Claims, R> claimResolver) {	
		final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
	}
	
	
	//extracting Username from extractClaim method because of magic of Functional Interface
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	
	//extracting expiration Date	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	
	//checking if the token is expired
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());		
	}
	
	
	//validating the token. with the "token ----> Username" and "DB/userDetails ----> Username".
	public boolean validateToken(String token, UserDetails userDetails) {
		//we need to compare the username/subject of token and username in DB matches and the token is not expired.
		String username = extractUsername(token);
		if(username.equals(userDetails.getUsername()) && !isTokenExpired(token)) {
			return true;
		}else {
			log.info("token validation Failed. Token expired or token does not belong to current user.");
			return false;
		}
	}
	

	
}//ends class
