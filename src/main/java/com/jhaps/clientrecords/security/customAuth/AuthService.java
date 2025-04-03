package com.jhaps.clientrecords.security.customAuth;

import org.springframework.security.core.userdetails.UserDetails;

import com.jhaps.clientrecords.dto.request.user.UserAuthRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	/*
	 * @param userDto contains the Email and password of User trying to login.
	 * @return  JWT token as a String.
	 * 
	 * */
	String verifyUser(UserAuthRequest userAuthRequest);
	
	
	String logOutUser(String authHeader, HttpServletRequest request, HttpServletResponse response, UserDetails userDetails);
	
	
	
	
}
