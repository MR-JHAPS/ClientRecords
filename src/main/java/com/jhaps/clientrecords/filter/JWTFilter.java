package com.jhaps.clientrecords.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jhaps.clientrecords.service.JWTServiceImpl;
import com.jhaps.clientrecords.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{

	@Autowired
	private JWTServiceImpl jwtServiceImpl;
	
	
	@Autowired
	ApplicationContext context;
	
//	@Autowired
//	UserDetailsServiceImpl userDetailsServiceImpl;
	
	
	/*THIS METHOD IS IMPLEMENTED FROM THE  "OncePerRequestFilter class" 
	even though it is class this  "method is abstract" 
	 and so we need to  "implement this abstract method" when we extend the OncePerRequestFilter class .
	*/
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		//Inside POSTMAN there is Header and inside the header there is 'Authorization', it refers to that field.
		String authHeader = request.getHeader("Authorization");
		String token ="";
		String username="";
		
		//we are checking if the auth header is not null
		if(authHeader!=null && authHeader.startsWith("Bearer ")) {
			
			token = authHeader.substring(7);
			
			//from the (token.claims/payload) we will extract the username.
			username = jwtServiceImpl.extractUsername(token);
			
			
		}
		
	/*    If the token contains a username and the user is not already authenticated:
	 * 	Because if the user is already authenticated in another filter we don't want to redo the same to prevent performance issues.
		       1.  Load the user details from the database using userDetailsService.
		       2.  Validate the token using jwtUtil.validateToken.
		       3.  Set the UsernamePasswordAuthenticationToken and then set that authentication to SecurityContextHolder.
	 */
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
		
				UserDetails userDetails = context.getBean(UserDetailsServiceImpl.class).loadUserByUsername(username);
			
					//if the token contains the userDetails and is valid.
				if(jwtServiceImpl.validateToken(token, userDetails)) {
					
					/*since the token contains the userDetails from the previous login page 
						we are saving that userdetails to the new instance of a token
						and setting the authentication in SecurityContext.
					*/
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}//ends inner if
			
		}//ends outer if
		

		
		
		
		filterChain.doFilter(request, response);
		
		
		
		
		
	}//ends method

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//ends class
