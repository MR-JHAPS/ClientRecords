package com.jhaps.clientrecords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

@SpringBootApplication
@EnableSpringDataWebSupport
public class ClientRecordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientRecordsApplication.class, args);
	}

}
