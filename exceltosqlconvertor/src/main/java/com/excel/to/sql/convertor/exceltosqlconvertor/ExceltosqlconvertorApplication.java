package com.excel.to.sql.convertor.exceltosqlconvertor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ExceltosqlconvertorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExceltosqlconvertorApplication.class, args);
	}

}
