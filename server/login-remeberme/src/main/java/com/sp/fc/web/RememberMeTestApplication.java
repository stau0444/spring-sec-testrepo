package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@SpringBootApplication(scanBasePackages = {"com.sp.fc.config","com.sp.fc.web"})
public class RememberMeTestApplication {
    HttpSessionSecurityContextRepository repository;
    UsernamePasswordAuthenticationFilter unfilter;
    SecurityContextPersistenceFilter filter;
    public static void main(String[] args) {
        SpringApplication.run(RememberMeTestApplication.class,args);
    }
}
