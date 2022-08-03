package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

// @SpringBootApplication(scanBasePackages={"com.example.demo.other_packages"})
@SpringBootApplication()
public class WebApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(WebApplication.class, args);
	}

}
