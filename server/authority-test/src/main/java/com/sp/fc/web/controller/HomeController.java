package com.sp.fc.web.controller;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    MethodSecurityInterceptor interceptor;

    @PreAuthorize("@nameCheck.check(#name)")
    @GetMapping("/greeting/{name}")
    public String greeting(@PathVariable String name){
        return "hello " +name+"!";
    }
}
