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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jhaps.clientrecords.filter.JWTFilter;
import com.jhaps.clientrecords.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JWTFilter jwtFilter;
	
//	@Autowired
//	private WebConfig corsConfig;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	 @Bean
//	    public AuthenticationProvider authenticationProvider() {
//	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//	        provider.setPasswordEncoder(passwordEncoder());
//	        provider.setUserDetailsService(userDetailsServiceImpl);
//
//
//	        return provider;
//	    }
	
//	@Bean 
//	public AuthenticationManager authManagerBuilder(HttpSecurity http) throws Exception {
//		
//		AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//			authBuilder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
//			return authBuilder.build();
//	}
//	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		
		return security
//				.cors(cors->cors.configurationSource(corsConfig.corsConfigurer()))// enable cors
				 .cors(Customizer.withDefaults())
				.csrf(csrf->csrf.disable())	
				.httpBasic(Customizer.withDefaults())//this is for the API's like Postman
				.authorizeHttpRequests(auth->auth
						.requestMatchers("/user/login").permitAll()
						.anyRequest().authenticated()  )

				.userDetailsService(userDetailsServiceImpl)
				
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				
				
				.build();
	}//ends SecurityChainFilter method.
	
	
	
	
	
	
	
}//ends class
