package com.jhaps.clientrecords.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration

public class SwaggerConfig {

	
	@Bean
	public OpenAPI myCustomConfig() {
		
		//This is to set the title and the description of the 
		return new OpenAPI()
				.info(
					new Info().title("Client List API's").description("By Neraz Oli")
				)
				.servers(
						List.of(
							new Server().url("http://localhost:8080").description("local"),
							new Server().url("http://localhost:8081").description("live")
						)
				)
				.addTagsItem(new Tag().name("Public Controller"))
				 
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.components(new Components().addSecuritySchemes(
						"bearerAuth", new SecurityScheme()
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")
										.in(SecurityScheme.In.HEADER)
										.name("Authorization")
						
				
				));
	}//ends method
	
	

	
	
	
	
	
	
	
}//ends class
