//package com.jhaps.clientrecords.config;
//
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//
//public class WebConfig implements WebMvcConfigurer{
////
////	 @Bean
////	    public CorsConfigurationSource corsConfigurer() {
////	        CorsConfiguration configuration = new CorsConfiguration();
////	        configuration.addAllowedOrigin("http://localhost:4209");  // Frontend URL
////	        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
////	        configuration.setAllowedHeaders(List.of("*"));
////	        configuration.setAllowCredentials(true);
////
////	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////	        source.registerCorsConfiguration("/**", configuration);
////	        return source;
////	    }
////}
//	
//	
//	
//	
//	// Allow requests from localhost:4200 (or your Angular frontend URL)
//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // Allow requests from localhost:4200 (or your Angular frontend URL)
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:4209")  // Add your frontend URL here
//                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow HTTP methods
//                .allowedHeaders("*"); 
//	
//	}
//}
