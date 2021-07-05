package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.sp.fc.config",
        "com.sp.fc.web"
})
public class UserDetailsTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDetailsTestApplication.class, args);
    }

}
