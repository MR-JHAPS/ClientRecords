package com.jhaps.clientrecords.enums;

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
	
	
	
	
}//ends enum
