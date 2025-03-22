package com.jhaps.clientrecords.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jhaps.clientrecords.entity.CustomUserDetails;

/*
 * This class extracts the security details
 *  from the SecurityContextHolder.
 */
public class SecurityUtils {

	
	
  		/* Extracts CustomUserDetails From SecurityContextHolder.*/
		public static CustomUserDetails getCustomUserDetailsFromSecurityContext() {
			return (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		
		/* Extracting user Email from the CustomUserDetails.*/
		public static String getEmailFromCustomUserDetails() {
			CustomUserDetails customUserDetails = getCustomUserDetailsFromSecurityContext();
			return customUserDetails.getUsername();
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
