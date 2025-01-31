package com.jhaps.clientrecords.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	
	private User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}


	//THIS IS TO GET THE DETAILS OF THE USER. if it has address, telephone age etc.
	public User getUser() {
		return user;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()) );
		
		
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
