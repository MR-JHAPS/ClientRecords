package com.jhaps.clientrecords.config;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

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
	OpenAPI myCustomConfig() {
		
		//This is to set the title and the description of the 
		return new OpenAPI()
				.info(
					new Info().title("Client List API's").description("By Neraz Oli")
				)
				
				.servers(
						List.of(
							new Server().url("http://localhost:8080").description("local")
//							new Server().url("http://localhost:8081").description("live")
						)
				)
			
				 
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
	
	
	

public OpenApiCustomizer makePublicTagFirst() {
return openApi -> {
	List<Tag> tags = openApi.getTags();
	if(tags!=null) {
		tags.sort(Comparator.comparing((Tag tag)->
			!"Public Controller".equals(tag.getName()))
				.thenComparing(Tag::getName));
	}
};
}
	


	
	
}//ends class



/*
 * TRIED SORTING USING 1.CONTROLLER API's but it wont work maybe because Swagger Overrides the custom sort.
 * 
 * Tried extracting the number from TAG.name and sorting with that number. It's not working.
 *
*/
//@Bean
//OpenApiCustomizer sortTags() {
//	return openApi -> {
//		List<Tag> tags = openApi.getTags();
//		if(tags!=null && !tags.isEmpty()) {
//			List<Tag> sortedTags = tags.stream()
//										.sorted(Comparator.comparingInt(
//														(tag) ->{
//															String[] splitedName = ((Tag)tag).getName().split("\\.");
//															int number = Integer.parseInt(splitedName[0]);
//															System.out.println(Arrays.toString(splitedName) );
//															System.out.println( number);
//															return number;
//														})//ends comparingInt 
//												)//ends sorted
//										.toList();
//			System.out.println(sortedTags);
//			openApi.setTags(sortedTags);
//		}//ends if
//	};//ends return
//}//ends method.
//
//


