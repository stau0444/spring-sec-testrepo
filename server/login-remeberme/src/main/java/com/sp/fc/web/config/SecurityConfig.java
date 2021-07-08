package com.sp.fc.web.config;

import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionManagementFilter;

import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import java.time.LocalDateTime;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    SessionManagementFilter smFilter;
    ConcurrentSessionFilter conFilter;
    RememberMeAuthenticationFilter filter;
    TokenBasedRememberMeServices services;
    private final UserService userService;
    private final DataSource datasource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //userDetailService 등
        auth.userDetailsService(userService);
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
                            .failureUrl("/login-error");
                }
        )
        .logout(logout-> logout.logoutSuccessUrl("/"))
        .exceptionHandling(e -> e.accessDeniedPage("/access-denied"))
        .rememberMe(r->r.rememberMeServices(rememberMeServices()))
        .sessionManagement(
                s->s.maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                    .expiredUrl("/session-expired")
        )
        ;

    }

    //리소스 폴더를 스프링시큐리티에서 제외시킴
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations(),
                PathRequest.toH2Console()
        );
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices(){
        return new PersistentTokenBasedRememberMeServices("hello",
                userService,tokenRepository());
    }


    @Bean
    PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(datasource);
        try {
            tokenRepository.removeUserTokens("1");
        }catch (Exception e){
            tokenRepository.setCreateTableOnStartup(true);
        }
        return tokenRepository;
    }
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher(){
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher(){
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                super.sessionCreated(event);
                System.out.printf("====> [%s] 세션 생성됨 %s \n" , LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent event) {
                super.sessionDestroyed(event);
                System.out.printf("====> [%s] 세션 만료됨 %s \n" , LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
                super.sessionIdChanged(event, oldSessionId);
                System.out.printf("====> [%s] 세션 아이디 변경 %s \n" , LocalDateTime.now(), event.getSession().getId());
            }
        });
    }
}
