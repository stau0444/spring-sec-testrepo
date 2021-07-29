package com.sp.fc.web.config;

import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Bean
    public void initDB(){
        userService.save(SpUser.builder()
                .email("user1")
                .password("1111")
                .enabled(true)
                .build());
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(),userService);
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(),userService);

        //Test에서 요청이 날라올때 필터체인으로 들어와서
        //아래 설정한 필터들을 하나씩 거치고
        http
                .csrf().disable()
                .sessionManagement(session->{
                    //세션을 쓰지 않도록 한다.
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                //로그인 처리
                //("/login"(POST)에 대한 요청이 여기서 걸린다.)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                //토큰을 검증
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class);

    }
}
