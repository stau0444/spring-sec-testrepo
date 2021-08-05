package com.sp.fc.web.config;


import com.sp.fc.user.domain.SpOAuth2User;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SpOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException
    {
        Object principal = authentication.getPrincipal();
        //구글 사용자임
        if(principal instanceof OidcUser){
            SpOAuth2User oauth = SpOAuth2User.OAuth2Provider.google.convert((OidcUser) principal);
            SpUser user = userService.load(oauth);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities())
            );

            //그외 사용자
        }else if(principal instanceof OAuth2User){
            SpOAuth2User oauth = SpOAuth2User.OAuth2Provider.naver.convert((OAuth2User) principal);
            SpUser user = userService.load(oauth);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities())
            );
        }
        request.getRequestDispatcher("/").forward(request, response);
    }
}
