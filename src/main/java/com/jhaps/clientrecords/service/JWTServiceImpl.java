package com.jhaps.clientrecords.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.management.RuntimeErrorException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl {

	private String secretKey ="";
	
	public JWTServiceImpl() {
		
	}
	
//------------ GENERATING JWT TOKEN --------------------------------------------------------------------------------------------------------------------------------	
	
	
	//This generates random    "SecretKey" type ------>  key.
	private SecretKey generateHmacKey() throws NoSuchAlgorithmException {	
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
		return keyGen.generateKey();
	}
	
	private String encodingSecretKeyBase64() {
		try {
			//calling the method "generateHmacKey" because it has generated A Secret Key.
			SecretKey secretKey = generateHmacKey();
//			String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded()); //.getEncoded converts from SecretKey type to byte[]
			String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			return encodedSecretKey;
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("cannot encode the SecretKey to the Base64 " + e);	
		}
	}//ends method
	
	
	//generating key for the signing/signature it will be of type Key .
	private SecretKey generateKeyForTokenSignature() {
		//getting the base64Encoded String secretKey.
		String secretKey = encodingSecretKeyBase64();
		byte[] secretKeyByte = Decoders.BASE64.decode(secretKey);
		SecretKey keyForSignature = Keys.hmacShaKeyFor(secretKeyByte);
		return keyForSignature;
	}
	
	
	
	public String generateJWTToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(email)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 *60))
				.and()
				.signWith(generateKeyForTokenSignature())
				.compact();

	}


	
//----- Validating Token ------------------------------------------------------------------------------------------------------------------------------------------

//this is to get the username from the JWT payload.
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	
	public <T> T extractClaim(String token , Function<Claims, T> claimResolver) {
		
		final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
        return Jwts.parser()
//                .verifyWith(getKey())
                .verifyWith(generateKeyForTokenSignature())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
	
	
	public boolean validateToken(String token, UserDetails userDetails) {
		
		//we need to compare the username/subject of token and username in DB matches and the token is not expired.
		String username = extractUsername(token);
		if(username.equals(userDetails.getUsername()) && !isTokenExpired(token)) {
			return true;
		}else {
			return false;
		}
	}
	
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	
	public boolean isTokenExpired(String token) {
		if(extractExpiration(token).before(new Date())){
			return true;
		}else {
			return false;
		}
		
		
	}
	
	
	
	
	
	
}
