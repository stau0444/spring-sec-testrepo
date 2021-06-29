package com.sp.fc.web.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FormLoginController {

    LogoutFilter logoutFilter;
    UsernamePasswordAuthenticationFilter filter;


    @GetMapping("/")
    public String main(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }


    @GetMapping("/login-error")
    public String longingError(Model model){
        model.addAttribute("loginError",true);
        return "loginForm";
    }
    @GetMapping("/access-denied")
    public String accessDenied(){
        return "AccessDenied";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user-page")
    public String userPage(){
        return "UserPage";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin-page")
    public String adminPage(){
        return "AdminPage";
    }


    @ResponseBody
    @GetMapping("/auth")
    public Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
