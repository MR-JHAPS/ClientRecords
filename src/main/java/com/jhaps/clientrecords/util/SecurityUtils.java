package com.jhaps.clientrecords.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jhaps.clientrecords.entity.CustomUserDetails;
import com.jhaps.clientrecords.entity.User;

/*
 * This class extracts the security details(CustomUserDetails)
 *  from the SecurityContextHolder.
 */
public class SecurityUtils {

	
	
  		/* Extracts CustomUserDetails From SecurityContextHolder.*/
		public static CustomUserDetails getCustomUserDetailsFromSecurityContext() {
			return (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		/* Extracts the User from CustomUserDetails */
		public static User getCurrentUser() {
			User currentUser = getCustomUserDetailsFromSecurityContext().getUser();
			return currentUser;
		}
		
		/* Extracting user Email from the CustomUserDetails.*/
		public static String getEmailFromCustomUserDetails() {
			CustomUserDetails customUserDetails = getCustomUserDetailsFromSecurityContext();
			return customUserDetails.getUsername(); // returns email
		}
		
		/* Extracting userId from the CustomUserDetails */
		public static int getUserIdFromCustomUserDetails() {
			CustomUserDetails customUserDetails = getCustomUserDetailsFromSecurityContext();
			int userId = customUserDetails.getUser().getId(); 
			return userId;
		}
		
		/* Extracting Authorities from the CustomUserDetails. */
		public static Set<String> getAuthoritiesFromCustomUserDetails(){
			CustomUserDetails customUserDetails = getCustomUserDetailsFromSecurityContext();
			Set<String> roles = customUserDetails.getAuthorities()
								.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toSet());
			return roles;
		}
			
	
	
	
	
	
}
