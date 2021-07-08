package com.sp.fc.web.config;

import com.sp.fc.web.controller.CanNotAccessUserPage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException
    {
        if(accessDeniedException instanceof CanNotAccessUserPage){
            request.setAttribute("error",accessDeniedException.getMessage());
            request.getRequestDispatcher("/access-denied").forward(request,response);
        }else {
            request.setAttribute("error","관리자 접근 권한 없음");
            request.getRequestDispatcher("/access-denied").forward(request,response);
        }
    }
}
