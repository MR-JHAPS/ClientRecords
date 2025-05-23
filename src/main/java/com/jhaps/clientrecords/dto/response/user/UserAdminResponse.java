package com.jhaps.clientrecords.dto.response.user;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This contains "Roles" of user and is specifically used to transfer user details to AdminDashboard
 * */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAdminResponse {

//	private int id;
//	
//	private String email;
//	
//	private Set<String> roles;
//	
//	private LocalDateTime createdOn;
//	
//	private LocalDateTime updatedOn;
	
	private int id;
	
	private String profileImageUrl;
	
	private String email;
	
	private Set<String> roles;
	
	private LocalDateTime createdOn;
	
	private LocalDateTime updatedOn;
	
	

}//ends class
