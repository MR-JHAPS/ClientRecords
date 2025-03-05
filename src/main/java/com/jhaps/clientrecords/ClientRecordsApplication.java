package com.jhaps.clientrecords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class ClientRecordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientRecordsApplication.class, args);
	}

}
