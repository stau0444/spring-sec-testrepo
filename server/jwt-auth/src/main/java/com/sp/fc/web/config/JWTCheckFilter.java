package com.sp.fc.web.config;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//리퀘스트가 올때 토큰을 검사해서
//시큐리티 컨텍스트 홀더에 Authentication을 올려주는 역할을 한다.
//BasicAuthenticationFilter 은 모든 요청에서 토큰을 감시한다.
public class JWTCheckFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    //토큰에 대한 검사를 하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearer == null || !bearer.startsWith("Bearer ")){
            //만약 비어러 토큰이 없다면 요청을 흘려보내서 다음 필터 혹은 인터셉터에서 인증을 받게한다.
            chain.doFilter(request,response);
            return;
        }

        //토큰이 있다면 Bearer를 때고
        String token = bearer.substring("Bearer ".length());
        //만들어 놓은 JWTUtil을 통해 토큰을 검증한다 .
        VerifyResult result = JWTUtil.verify(token);

        //만약 검증에 성공한다면
        if (result.isSuccess()){
            //토큰에서 username을 꺼내고 userService로 찾아본다.
            SpUser user = (SpUser) userService.loadUserByUsername(result.getUsername());
            //null 처리 필요
            //새로운 토큰을 만들어서
            UsernamePasswordAuthenticationToken verifiedToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),null,user.getAuthorities()
            );
            //시큐리티 컨텍스트 홀더에 올려놓으면 요청을 수행할 수 있다 .
            SecurityContextHolder.getContext().setAuthentication(verifiedToken);
            //요청을 다음으로 넘긴다.
            chain.doFilter(request,response);
        }else {
            //401 error 발생
            throw new TokenExpiredException("token expired");        }
    }
}
