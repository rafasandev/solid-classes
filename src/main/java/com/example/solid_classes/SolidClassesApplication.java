package com.example.solid_classes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaAuditing
@RestController
public class SolidClassesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolidClassesApplication.class, args);
	}

	@GetMapping("/")
	public String getMethodName() {
		return "Olá mundo";
	}

}
