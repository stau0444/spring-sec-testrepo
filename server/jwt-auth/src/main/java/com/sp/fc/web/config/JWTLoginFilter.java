package com.sp.fc.web.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.fc.user.domain.SpUser;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//UsernamePassword필터의 기반으로 한다.
//UsernamePassword를 체크하고 인증이 성공하면 JWT 토큰을 넘겨준다
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        //longin post 요청을 처리
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        //리퀘스트에서 인풋스트림을 읽어 objectmapper를 통해 UserLoginForm으로 변환한다.
        UserLoginForm userLoginForm = null;
        try {
            userLoginForm = objectMapper.readValue(request.getInputStream(), UserLoginForm.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //받은 값으로 로그인을 할 수 있게 토큰을 발행한다 .
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userLoginForm.getUsername(),userLoginForm.getPassword()
        );

        // AuthenticationManager 가 DaoAuthenticationProvider를 통해
        // UserService로 유저를 검증하고  성공시 해당 유저를 리턴해준다.
        return getAuthenticationManager().authenticate(token);
    }

    //인증 성공시 아래 메서드로 들어온다
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        //성공시 위에서 리턴한 유저가 여기 담긴다
        SpUser user = (SpUser) authResult.getPrincipal();

        //토큰을 심어준다.
        response.setHeader(HttpHeaders.AUTHORIZATION,"Bearer " + JWTUtil.createAuthToken(user));
        //response 헤더에 컨텐츠타입을 json으로 지정한다.
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        //response outputstream에 user를 써서 내려준다 .
        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
    }
}
