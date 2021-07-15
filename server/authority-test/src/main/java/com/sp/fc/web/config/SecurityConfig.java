package com.sp.fc.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    FilterSecurityInterceptor interceptor;




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("student1")
                                .password("1111")
                                .roles("USER","STUDENT")
                )
                .withUser(
                        User.withDefaultPasswordEncoder()
                        .username("student2")
                        .password("1111")
                        .roles("USER","STUDENT")
                )
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("tutor1")
                                .password("1111")
                                .roles("USER","TUTOR")
                )
                .withUser(
                User.withDefaultPasswordEncoder()
                        .username("primary")
                        .password("1111")
                        .roles("USER","PRIMARY")
        );
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().and()
                .authorizeRequests(r ->
                                    r.mvcMatchers("/greeting/{name}")
                                            .access("@nameCheck.check(#name)")
                                            .anyRequest().authenticated()
                );
    }
}
