package com.sp.fc.web.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.format.DataFormatMatcher;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.Encoder;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ObjectMapper om;

    @RequestMapping("/")
    public String index(){
        return "this is homepage";
    }

    @RequestMapping("/auth")
    public Authentication auth(){

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        return authentication;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping("/user")
    public SecurityMessage user(){
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("user 정보")
                .build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public SecurityMessage admin(){
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("admin 정보")
                .build();
    }
}
