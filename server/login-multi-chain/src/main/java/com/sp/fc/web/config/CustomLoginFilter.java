package com.sp.fc.web.config;

import com.sp.fc.web.student.StudentAuthenticationToken;
import com.sp.fc.web.teacher.Teacher;
import com.sp.fc.web.teacher.TeacherAuthenticationToken;
import ognl.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// 필터를 직접 정의한다.
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    //필터는
    public CustomLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        username = (username != null) ? username : "";
        username = username.trim();
        String password = obtainPassword(request);
        password = (password != null) ? password : "";

        String type = request.getParameter("type");
        if (type == null || type.equals("teacher")){
            TeacherAuthenticationToken token = TeacherAuthenticationToken.builder()
                    .credentials(username)
                    .build();
            return this.getAuthenticationManager().authenticate(token);
        }else{
            StudentAuthenticationToken token = StudentAuthenticationToken.builder()
                    .credentials(username)
                    .build();
            return this.getAuthenticationManager().authenticate(token);

        }

    }
}
