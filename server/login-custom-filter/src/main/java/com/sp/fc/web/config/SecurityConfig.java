package com.sp.fc.web.config;

import com.sp.fc.web.student.StudentManager;
import com.sp.fc.web.teacher.TeacherManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


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
        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());
        http
                .authorizeRequests(request->
                        request.antMatchers("/","/login").permitAll()
                                .anyRequest().authenticated()
                )
//        .formLogin( login ->{
//            login.loginPage("/login").permitAll()
//            .defaultSuccessUrl("/",false)
//            .failureUrl("/login-error");
//        })
        .addFilterAt(filter , UsernamePasswordAuthenticationFilter.class)
        .logout(logout->logout.logoutSuccessUrl("/"))
        .exceptionHandling(e -> e.accessDeniedPage("/access-denied"));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                ;
    }
}
