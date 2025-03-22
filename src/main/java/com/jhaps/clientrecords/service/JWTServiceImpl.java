package com.jhaps.clientrecords.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl {

//	private String secretKey ="5IPbklh1IEQOxzN4UUqb4qaCG7r6e3UQsIE2+XLAi96q+LRm4VvBY76ox7PuaCNtQeJdEiYF/04X/VJbC6LXKPYohgfDF256PLOHnW1z/Pk5woh91PIsFyg28e6uXv0WSmZW1E7QtXl50HsnkN6vtr70Wzk/ws35IFi+HfA6gE4c09nwcB0+yn2wOIMSlXinb578qtgtxKGvYmnz6hQcbaqoh0VXsA3pGVKkdIeYbsxNHVnwg/wZhmltYrd4suPUC0M5nBtXAwk8G0D5coqGk12n3ttLCPdsRBVR8GN8aa6hL+FZYwAU5rDHk4X7yN4xJGTOJ7SgjGD7DJ6sVNdA";
	@Value("${jwt.secret.key}")
private String secretKey ;
//------------ GENERATING JWT TOKEN --------------------------------------------------------------------------------------------------------------------------------	
	

	
	
	
	
	//generating key for the signing/signature it will be of type Key .
		private SecretKey generateKeyForTokenSignature() {
			//converting our string to byte because To convert it to secretKey of HmacShaKey.
			byte[] secretKeyByte = Decoders.BASE64.decode(secretKey);
//			SecretKey keyForSignature = Keys.hmacShaKeyFor(secretKeyByte);
//			return keyForSignature;
			return Keys.hmacShaKeyFor(secretKeyByte);
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


	
	//Extracting All Claims from token
	private Claims extractAllClaims(String token) {
		System.out.println("I am inside the extract all claims token : " + token);
        return Jwts.parser()
                .verifyWith(generateKeyForTokenSignature())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
	
	
	
	
	//method that implements the Functional Interface and allows the extraction of username, exp date, issu date.
	public <T> T extractClaim(String token , Function<Claims, T> claimResolver) {	
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
	
	//validating the token. with the "token ----> Username" and   "DB/userDetails ----> Username".
	public boolean validateToken(String token, UserDetails userDetails) {
		//we need to compare the username/subject of token and username in DB matches and the token is not expired.
		String username = extractUsername(token);
		if(username.equals(userDetails.getUsername()) && !isTokenExpired(token)) {
			return true;
		}else {
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
}
