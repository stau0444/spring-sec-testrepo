package com.sp.fc.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //등록시 비밀번호를 암호화 하지 않으면 등록이 안된다.
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("ugo2")
                        .password(passwordEncoder().encode("asdasd1"))
                        .roles("USER"))
                .withUser(User.builder()
                        .username("ugo3")
                        .password(passwordEncoder().encode("asdasd1"))
                        .roles("ADMIN"));

        //super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests((request)->
            request.antMatchers("/").permitAll()
                    .anyRequest().authenticated()
        );
        http.formLogin(login ->{
            login.defaultSuccessUrl("/",false);
        });
        http.httpBasic();

        //super.configure(http);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
