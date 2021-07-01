package com.sp.fc.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthDetails customAuthDetails;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //등록시 비밀번호를 암호화 하지 않으면 등록이 안된다.
        auth.inMemoryAuthentication()
                .withUser(
                        User.builder()
                        .username("ugo")
                        .password(passwordEncoder().encode("asdasd1"))
                        .roles("USER")
                )
                .withUser(
                        User.builder()
                        .username("ugo1")
                        .password(passwordEncoder().encode("asdasd1"))
                        .roles("ADMIN")
                );

        //super.configure(auth);
    }

    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests(request ->{
                request.antMatchers("/").permitAll();
                request.anyRequest().authenticated();
            }
        )
       .formLogin(
                login ->{
                    login.loginPage("/login")
                            .permitAll()
                            .defaultSuccessUrl("/",false)
                            .failureUrl("/login-error")
                            .authenticationDetailsSource(customAuthDetails);
                }
        )
        .logout(logout-> logout.logoutSuccessUrl("/"))
        .exceptionHandling(e -> e.accessDeniedPage("/access-denied"));

    }

    //리소스 폴더를 스프링시큐리티에서 제외시킴
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
        );
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
