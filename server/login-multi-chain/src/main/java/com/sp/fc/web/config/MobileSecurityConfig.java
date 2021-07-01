package com.sp.fc.web.config;

import com.sp.fc.web.student.StudentManager;
import com.sp.fc.web.teacher.TeacherManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Order(1)
@Configuration
@RequiredArgsConstructor
public class MobileSecurityConfig extends WebSecurityConfigurerAdapter {

    //학생의 인증을 제공할 authenticationProvider
    private final StudentManager studentManager;
    //선생님의 인증을 제공할 authenticationProvider
    private final TeacherManager teacherManager;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //학생 선생님 authenticationProvider를 AuthenticationManager에 등록
        auth.authenticationProvider(studentManager);
        auth.authenticationProvider(teacherManager);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .csrf().disable()
                .authorizeRequests(
                        request-> request.anyRequest().authenticated()
                )
                .cors().disable()
                .httpBasic();
    }
}
