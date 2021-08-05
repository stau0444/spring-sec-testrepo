package com.sp.fc.web.config;


import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SnsSecurityConfig extends WebSecurityConfigurerAdapter {


    private final SpOAuth2SuccessHandler successHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .oauth2Login(
                    oauth2->{
                        oauth2
                                .successHandler(successHandler);
                    }
                );
    }
}
