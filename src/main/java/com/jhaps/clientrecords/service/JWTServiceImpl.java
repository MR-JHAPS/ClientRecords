package com.jhaps.clientrecords.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl {

	private String secretKey ="";
	
	public JWTServiceImpl() {
		
	}
	
	//This generates random    "SecretKey" type ------>  key.
	private SecretKey generateHmacKey() throws NoSuchAlgorithmException {	
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
		return keyGen.generateKey();
	}
	
	private String encodingSecretKeyBase64() {
		try {
			//calling the method "generateHmacKey" because it has generated A Secret Key.
			SecretKey secretKey = generateHmacKey();
			String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded()); //.getEncoded converts from SecretKey type to byte[]
			return encodedSecretKey;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("cannot encode the SecretKey to the Base64 " + e);	
		}
	}//ends method
	
	
	//generating key for the signing/signature it will be of type Key .
	private Key generateKeyForTokenSignature() {
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	private HashMap<String, Object> buildClaim(){
//		
//		Map<String, Object> claims = new HashMap<>();
//		
//		
//		
//		
//	}
	
	
//	private String creatingToken
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	private String secretKey= "";
//	
//	public JWTServiceImpl() {
//		try {
//			//defining type of keyGenerator we want with "HmacSHA256" algorithm.
//			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//			//generating a key using above algorithm.
//			SecretKey sk = keyGen.generateKey();
//			
//			/*  "sk.getEncoded"  ------> transform sk to byte[].
//			 *  "Base64.getEncoder().encodeToString"  ------> this converts the byte[] to string using Base64 encoder.
//			 * */
//			
//			 secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//					
//					
//		}catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public String generateToken(String email) {
//		// we are building claims here i.e "payload" in JWT 
//		Map<String, Object> claims = new HashMap<>();
//		return Jwts.builder()
//				.claims()
//				.add(claims)
//				.subject(email)
//				.issuedAt( new Date(System.currentTimeMillis()))
//				.expiration( new Date(System.currentTimeMillis() + 1000 * 60 * 10))
//				.and()
//				.signWith(getKey())
//				.compact();
//				
//	}
//
//	private Key getKey() {
//		
//		//byte[] keyByte = secretKey.getBytes();
//		/* We have encoded "sk.getencoded" from byte[] to Base64 encoder string 
//		 * so we are decoding it back to byte so that we can pass it to "Keys.hmacShaKeyFor(keyByte)".
//		 * */
//		byte[] keyByte = Decoders.BASE64.decode(secretKey);
//		return Keys.hmacShaKeyFor(keyByte);
//	}
//	
	
	
	
	
}
