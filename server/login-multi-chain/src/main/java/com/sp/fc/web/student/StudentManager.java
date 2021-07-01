package com.sp.fc.web.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class StudentManager implements AuthenticationProvider , InitializingBean {

    UsernamePasswordAuthenticationFilter filter;

    private  HashMap<String,Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)  authentication;
        if(studentDB.containsKey(token.getCredentials()) ){
            Student student = studentDB.get(token.getCredentials());
            return StudentAuthenticationToken.builder()
                    .principal(student)
                    .details(student.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == StudentAuthenticationToken.class ||
                authentication == UsernamePasswordAuthenticationToken.class;
    }

    public List<Student> myStudent(String teacherId){
        return studentDB.values().stream().filter(student -> student.getTeacherId().equals("gang"))
                .collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Student("ugo","우고",Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),"gang"),
                new Student("hwang","황방",Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),"gang"),
                new Student("kong","공길",Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),"gang")
        ).forEach(s -> studentDB.put(s.getId(),s));
    }
}
