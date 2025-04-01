package com.jhaps.clientrecords.security.customAuth;

import com.jhaps.clientrecords.dto.request.user.UserAuthRequest;

public interface AuthService {
	/*
	 * @param userDto contains the Email and password of User trying to login.
	 * @return  JWT token as a String.
	 * 
	 * */
	String verifyUser(UserAuthRequest userAuthRequest);
	
	
	
}
