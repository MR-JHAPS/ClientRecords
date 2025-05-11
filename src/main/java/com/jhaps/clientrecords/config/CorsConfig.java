package com.jhaps.clientrecords.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jhaps.clientrecords.util.ImageUploadPath;

@Configuration
public class CorsConfig implements WebMvcConfigurer{

	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/images/**")
	               .addResourceLocations("file:"+ImageUploadPath.PATH.getPath())
	               .setCachePeriod(3600);
	    }
	
	
	  @Bean
	   WebMvcConfigurer corsConfigurer() {
	    return new WebMvcConfigurer() {
	      @Override
	      public void addCorsMappings(CorsRegistry registry) {
	        registry
            
	            .addMapping("/**") // apply to all endpoints
	            .allowedOrigins(
	                "http://localhost:4200",
	                "https://playful-pony-0dfcda.netlify.app"
	        )
	            .allowedMethods("*") // GET, POST, PUT, DELETE, etc.
	            .allowedHeaders("*")
	            .allowCredentials(true);
	        
	      }
	    };
	  }//ends method
	
}//ends class
