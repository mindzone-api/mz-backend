package com.mindzone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MindzoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindzoneApplication.class, args);
	}

}
