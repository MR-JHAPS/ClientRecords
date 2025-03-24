package com.jhaps.clientrecords.service;

import com.jhaps.clientrecords.dto.UserDto;

public interface AuthService {
	/*
	 * @param userDto contains the Email and password of User trying to login.
	 * @return  JWT token as a String.
	 * 
	 * */
	String verifyUser(UserDto userDto);
	
	
	
}
