package com.heliversetask;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "Heliverse-Task By Pranav Bisaria",version = "3.0.6",description = "This is a springboot application for  timed quiz, the application contains all the required api, with a scheduler that updates the database after fixed interval of time, also the application includes rate limiter by using resilience4j and caching using Guava cache."))
public class HeliverseTaskApplication {
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(HeliverseTaskApplication.class, args);
	}

}
