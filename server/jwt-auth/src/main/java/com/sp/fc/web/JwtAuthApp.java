package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.sp.fc.config",
        "com.sp.fc.web"
})
public class JwtAuthApp {
    public static void main(String[] args) {
        SpringApplication.run(JwtAuthApp.class,args);
    }
}
