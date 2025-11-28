package com.example.solid_classes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SolidClassesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolidClassesApplication.class, args);
    }
}
