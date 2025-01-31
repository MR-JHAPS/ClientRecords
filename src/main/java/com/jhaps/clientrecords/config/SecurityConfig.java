package com.jhaps.clientrecords.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jhaps.clientrecords.filter.JWTFilter;
import com.jhaps.clientrecords.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//	@Autowired
//	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JWTFilter jwtFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		
		return security
				.csrf(csrf->csrf.disable())	
				.httpBasic(Customizer.withDefaults())//this is for the API's like Postman
				.authorizeHttpRequests(auth->auth
						.requestMatchers("/user/login").permitAll()
						.anyRequest().hasAuthority("admin")  	)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				
				
				.build();
	}//ends SecurityChainFilter method.
	
	
	
	
	
	
	
}//ends class
