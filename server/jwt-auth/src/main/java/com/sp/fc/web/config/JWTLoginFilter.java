package com.sp.fc.web.config;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.domain.Token;
import com.sp.fc.user.service.TokenService;
import com.sp.fc.user.service.UserService;
import com.sp.fc.user.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService userService;
    private TokenService tokenService;

    public JWTLoginFilter(AuthenticationManager authenticationManager,UserService userService,TokenService tokenService) {
        super(authenticationManager);
        this.userService = userService;
        this.tokenService = tokenService;
        //longin post 요청을 처리
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        //리퀘스트에서 인풋스트림을 읽어 objectmapper를 통해 UserLoginForm으로 변환한다.
        UserLoginForm userLoginForm = null;
        String refresh_token = request.getHeader("refresh_token");

        if (refresh_token == null){
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

        }else {
            // 여기서 부터는 refresh 토큰으로 들어왔을때의 상황이다
            // refresh 토큰이 들어온다는 것은 auth 토큰의 유효기간이 지난 것이다 .

            //JWTUtil이 검증할 수 있게 자르고
            String refreshToken = refresh_token.substring("bearer ".length());
            //리프레시 토큰을 검증한다 .
            VerifyResult verify = JWTUtil.verify(refreshToken);


            //발급 받을때 DB에 토큰이 저장되기 떄문에
            //발급 받은 토큰이 DB에 있는지 확인한다
            tokenService.findToken(verify.getUsername(),refreshToken).orElseThrow(
                        //없을 경우 토큰의 일관성이 없어진 것으로 간주되며
                        //DB에 해당 유저이름으로 저장된 토큰이 지워지고 예외가 터진다
                        ()->{
                            tokenService.deleteAll(verify.getUsername());
                            throw new TokenExpiredException("토큰 탈취됨");
                        }
                    );

            //위에서 예외가 터지지 않았다면 토큰이 일치하는 것이니 아래 로직으로 넘어온다
            if(verify.isSuccess()){
                SpUser user = (SpUser) userService.loadUserByUsername(verify.getUsername());
                return new UsernamePasswordAuthenticationToken(user, user.getAuthorities());
            }else {
                throw new TokenExpiredException("refresh token expired");
            }
        }
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
        String refreshToken = JWTUtil.createRefreshToken(user);

        //토큰을 심어준다.
        response.setHeader("auth_token",JWTUtil.createAuthToken(user));
        response.setHeader("refresh_token",refreshToken);
        //response 헤더에 컨텐츠타입을 json으로 지정한다.
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        //response outputstream에 user를 써서 내려준다 .
        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
        tokenService.saveToken(Token.builder().username(user.getUsername()).token(refreshToken).build());
    }
}
