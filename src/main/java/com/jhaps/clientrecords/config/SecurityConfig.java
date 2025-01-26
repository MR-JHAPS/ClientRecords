package com.jhaps.clientrecords.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
		return auth.getAuthenticationManager();
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		
		return security
				.authorizeHttpRequests(auth->auth
//										.anyRequest().permitAll()
						.anyRequest().authenticated()
				
						)
				//this is for the API's like Postman
				.httpBasic(Customizer.withDefaults())
				.csrf(csrf->csrf.disable())
				
				.build();
		
		
	}//ends filter method
	
	
	
	
	
	
	
}//ends class
