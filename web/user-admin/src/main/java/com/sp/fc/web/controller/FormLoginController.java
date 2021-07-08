package com.sp.fc.web.controller;

import com.sp.fc.user.domain.SpAuthority;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.repository.AuthRepository;
import com.sp.fc.user.repository.UserRepository;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@RequiredArgsConstructor
public class FormLoginController {

    private final UserService service;
    private final AuthRepository repository;
    LogoutFilter logoutFilter;
    UsernamePasswordAuthenticationFilter filter;



    @GetMapping("/")
    public String main(Model model, HttpSession session){
        model.addAttribute("sessionId","sessionId= "+session.getId());
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
    public String userPage(@AuthenticationPrincipal SpUser user){
        Set<SpAuthority> authorities = user.getAuthorities();

        SpAuthority authority = repository.findByAuthorityAndAndUserId("ROLE_USER",2L);

        System.out.println("isContain = " + authorities.contains(authority));
        if(!hasAuthority(user)){
            System.out.println("들어옴");
            throw new CanNotAccessUserPage("유저 페이지 접근권한이 없습니다");
        }
        return "UserPage";
    }

    private boolean hasAuthority(SpUser user) {
        Set<SpAuthority> authorities = user.getAuthorities();
        boolean hasAuthority = false;
        for (SpAuthority authority : authorities) {
            if(authority.getAuthority().equals("ROLE_USER")){
                hasAuthority = true;
            }
        }
        return hasAuthority;
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
