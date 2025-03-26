package com.jhaps.clientrecords.service;

import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.User;

/*
 * This handles User Security related logic like :
 * 	
 * 	- updateLoginAttempts(),
 *  - lockAccount(),
 *  - unlockAccount(),
 *  - validatePassword(),
 *  - resetLoginAttempts()
 * 
 * */

public interface UserSecurityService {

	public void updateLoginAttempts(UserDto userDto); /* After each wrong password attempt user->attempt field will be updated till it reaches 3*/
	
	public void unlockAccount(int id);	/* ADMIN - Unlock the locked User Account by ID */
	
	public void lockAccount(int id);	/* ADMIN - Lock the user Account by ID*/
	
	public void resetLoginAttempts(String email);
	
	public boolean unlockAfterGivenTime(User user);
	
	
	
}
