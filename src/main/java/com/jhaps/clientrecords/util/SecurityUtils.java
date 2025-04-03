package com.jhaps.clientrecords.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.security.model.CustomUserDetails;

/*
 * This class extracts the security details(CustomUserDetails)
 *  from the SecurityContextHolder.
 */
@Component
public class SecurityUtils {

	
	
  		/* Extracts CustomUserDetails From SecurityContextHolder.*/
		public  CustomUserDetails getCustomUserDetailsFromSecurityContext() {
			return (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		/* Extracts the User from CustomUserDetails */
		public  User getCurrentUser() {
			User currentUser = getCustomUserDetailsFromSecurityContext().getUser();
			return currentUser;
		}
		
		/* Extracting user Email from the CustomUserDetails.*/
		public  String getEmailFromCustomUserDetails() {
			CustomUserDetails customUserDetails = getCustomUserDetailsFromSecurityContext();
			return customUserDetails.getUsername(); // returns email
		}
		
		/* Extracting userId from the CustomUserDetails */
		public  int getUserIdFromCustomUserDetails() {
			CustomUserDetails customUserDetails = getCustomUserDetailsFromSecurityContext();
			int userId = customUserDetails.getUser().getId(); 
			return userId;
		}
		
		/* Extracting Authorities from the CustomUserDetails. */
		public  Set<String> getAuthoritiesFromCustomUserDetails(){
			CustomUserDetails customUserDetails = getCustomUserDetailsFromSecurityContext();
			Set<String> roles = customUserDetails.getAuthorities()
								.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toSet());
			return roles;
		}
			
	
	
	
	
	
}
