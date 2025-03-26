package com.jhaps.clientrecords.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jhaps.clientrecords.serviceImpl.JWTServiceImpl;
import com.jhaps.clientrecords.serviceImpl.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{

	@Autowired
	private JWTServiceImpl jwtServiceImpl;
	
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	//THIS IS TO SKIP THE JWT FILTER FOR "/user/login" page to disable the JWT interference for login.
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String req = request.getServletPath(); 
		return req.equals("/api/public/login") || 
		req.equals("/api/public/signup") ||
		req.contains("swagger-ui")||
		req.contains("api-docs");
	}

	
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
	 * 		Because if the user is already authenticated in another filter we don't want to redo the same to prevent performance issues.
		       1.  Load the user details from the database using userDetailsService.
		       2.  Validate the token using jwtUtil.validateToken.
		       3.  Set the UsernamePasswordAuthenticationToken and then set that authentication to SecurityContextHolder.
	 */
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);	
					//if the token contains the userDetails and is valid.
				if(jwtServiceImpl.validateToken(token, userDetails)) {	
					/*since the token contains the userDetails from the previous login page 
						we are saving that userdetails to the new instance of a token
						and setting the authentication in SecurityContext.
					*/
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());
//					 authToken.setDetails(new WebAuthenticationDetailsSource()
//		                        .buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}//ends inner if
		}//ends outer if
		filterChain.doFilter(request, response);	
	}//ends method

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//ends class
