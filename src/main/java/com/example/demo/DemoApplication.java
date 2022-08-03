package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.example.demo.quickstart.GirlFriend;
import com.example.demo.quickstart.other_packages.Bikini;
// Only get beans in scope 'com.company.demo.other_packages'
// using @ComponentScan or scanBasePackages parameter of @SpringBootApplication
// @ComponentScan("com.example.demo.other_packages")
// @ComponentScan({"com.example.demo.other_packages"})
// @SpringBootApplication(scanBasePackages={"com.example.demo.other_packages"})
@SpringBootApplication()
public class DemoApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		// Dress dress1 = context.getBean(Dress.class);
		// Dress dress2 = context.getBean(Dress.class);

		// System.out.println("dress 1 => " + dress1);
		// System.out.println("dress 2 => " + dress2);

		// Outfit outfit = context.getBean(Outfit.class);
		// System.out.println("outfit => " + outfit);
		// outfit.wear();

		// Bikini bikini = context.getBean(Bikini.class);
		// System.out.println("bikini => " + bikini);
		// bikini.wear();

		GirlFriend girlfriend = context.getBean(GirlFriend.class);
		System.out.println("girlfriend => " + girlfriend);
		girlfriend.outfit.wear();

	}

}
