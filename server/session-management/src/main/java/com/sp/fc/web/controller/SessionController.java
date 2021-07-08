package com.sp.fc.web.controller;

import com.sp.fc.user.domain.SpUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SessionController {

    private final SessionRegistry registry;


    @GetMapping("/session-list")
    public String sessions(Model model){


        List<UserSession> userSessions = registry.getAllPrincipals().stream()
                .map(p -> UserSession.builder()
                        .username(((SpUser)p).getUsername())
                        .sessions(registry.getAllSessions(p,false)
                                .stream().map(si ->SessionInfo.builder()
                                        .lastRequest(si.getLastRequest())
                                        .sessionId(si.getSessionId())
                                        .build()
                                ).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("sessionList",userSessions);

        return "sessionList";
    }

    @PostMapping("/session/expire")
    public String expireSession(@RequestParam String sessionId){
        SessionInformation session = registry.getSessionInformation(sessionId);
        if(!session.isExpired() == true){
            session.expireNow();
        }
        return "redirect:/session-list";
    }

    @GetMapping("/session-expired")
    public String sessionExpired(){
        return "sessionExpired";
    }

}
