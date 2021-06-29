package com.sp.fc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormLoginController {

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
    @GetMapping("/user-page")
    public String userPage(){
        return "UserPage";
    }
    @GetMapping("/admin-page")
    public String adminPage(){
        return "AdminPage";
    }
}
