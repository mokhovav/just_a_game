package com.mokhovav.justAGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.mokhovav.inspiration", "com.mokhovav.base_spring_boot_project", "com.mokhovav.justAGame"})
public class JustAGame {
    public static void main(String[] args) {
        SpringApplication.run(JustAGame.class, args);
    }
}