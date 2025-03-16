package com.jhaps.clientrecords.enums;

import java.util.HashSet;
import java.util.Set;

public enum RoleNames {
	
	ADMIN("admin"),
	USER("user"),
	BLOCKED("blocked");
	
	private final String role; //field

	
	//constructor
	private RoleNames(String role) {
		this.role = role;
	}
	
	//getter
	public String getRole() {
		return this.role;
	}
	
	
//	// Saving all the roles in Set<String>.
//	public static Set<String> getRoles(){
//		Set<String> roles = new HashSet<>();
//		for(RoleNames role : RoleNames.values()) {
//			
//			roles.add(role.toString());
//		}
//		return roles;
//	}
	
	
	
	
}//ends enum
