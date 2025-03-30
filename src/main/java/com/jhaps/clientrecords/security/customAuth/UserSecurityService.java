package com.jhaps.clientrecords.security.customAuth;

import com.jhaps.clientrecords.dto.request.user.UserAuth;
import com.jhaps.clientrecords.dto.response.UserDto;
import com.jhaps.clientrecords.entity.system.User;

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

	public void updateLoginAttempts(UserAuth userAuth); /* After each wrong password attempt user->attempt field will be updated till it reaches 3*/
	
	public void unlockAccount(int id);	/* ADMIN - Unlock the locked User Account by ID */
	
	public void lockAccount(int id);	/* ADMIN - Lock the user Account by ID*/
	
	public void resetLoginAttempts(String email);
	
	public boolean unlockAfterGivenTime(User user);
	
	
	
}
