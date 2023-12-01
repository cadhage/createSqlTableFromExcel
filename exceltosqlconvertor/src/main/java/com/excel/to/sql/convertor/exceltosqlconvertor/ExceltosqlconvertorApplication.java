package com.excel.to.sql.convertor.exceltosqlconvertor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ExceltosqlconvertorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExceltosqlconvertorApplication.class, args);
	}
    @Bean
    public org.springframework.web.filter.CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow requests from a specific origin
        config.setAllowedOrigins(List.of("http://localhost:5173")); // Add your frontend URL

        // Allow specific methods (GET, POST, etc.) and headers
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
