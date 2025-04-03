package com.jhaps.clientrecords.security.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jhaps.clientrecords.entity.system.User;

public class CustomUserDetails implements UserDetails {

	
	private User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}


	//THIS IS TO GET THE DETAILS OF THE USER. if it has address, telephone age etc.
	public User getUser() {
		return user;
	}
	

	//For Multiple Roles
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles()
					.stream()
					.map(role ->new SimpleGrantedAuthority(role.getName()))
					.collect(Collectors.toSet()); //collecting in a "Set" instead of "List".
	}


	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	
	
	//For single Role.
	//	@Override
	//	public Collection<? extends GrantedAuthority> getAuthorities() {		
	//		return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()) );
	//	}
	
	
	
	
	
	
	
	
	
}
