package com.jhaps.clientrecords.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jhaps.clientrecords.security.jwt.JWTFilter;
import com.jhaps.clientrecords.serviceImpl.system.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JWTFilter jwtFilter;
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		
		return security
				.cors(Customizer.withDefaults()) //I have created a custom Cors in "CorsConfig.java".
				.csrf(csrf->csrf.disable())	
				.httpBasic(Customizer.withDefaults())//this is for the API's user like Postman.
				.userDetailsService(userDetailsServiceImpl)
				.authorizeHttpRequests(auth->auth
						.requestMatchers("/api/public/**", "/api/public/login").permitAll() //for userLogin
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**",  "/swagger-ui.html", "/swagger-ui/index.html").permitAll() //for Swagger	 
						.requestMatchers("/api/roles/**", "/api/admin/**").hasAuthority("admin")
						.requestMatchers("/images/**").permitAll() // allow image access
						
						.anyRequest().authenticated() 
				)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) 
				/* we are adding jwtToken before the username password authentication.
				 * In JwtFilter we have a --"shouldNotFilter()"-- method that allows some requests
				 * like --"/user/login"-- to be accessed without JwtToken
				 * i have permitted "Swagger-url & login-url in both SpringSecurityConfig and 
				 * JwtFiler so that it does not require username, password or JwtToken.
				 * */
				.build();
	}//ends SecurityChainFilter method.
	


	
	
}//ends class
