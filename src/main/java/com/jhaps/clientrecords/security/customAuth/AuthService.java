package com.jhaps.clientrecords.security.customAuth;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.jhaps.clientrecords.dto.request.user.UserAuthRequest;
import com.jhaps.clientrecords.dto.response.LoginResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	/*
	 * @param userDto contains the Email and password of User trying to login.
	 * @return  JWT token(String), refreshToken(String) and emailVerificationStatus(boolean).
	 * 
	 * */
	LoginResponse handleLoginRequest(UserAuthRequest userAuthRequest);
	
	
//	String logOutUser(String authHeader, HttpServletRequest request, HttpServletResponse response, UserDetails userDetails);
	
	
	boolean validateToken(String token, UserDetails userDetails);
	

	
}
