package com.sp.fc.web.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class TeacherManager implements AuthenticationProvider , InitializingBean {

    //Teacher를 담는 테스트DB 역할을 한다.
    private HashMap<String,Teacher> teacherDB = new HashMap<>();

    //
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //authentication이 UsernamePasswordAuthenticationToken일 경우
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if(teacherDB.containsKey(token.getName())){
                return getAuthenticationToken(token.getName());
            }
         }

        //authentication이 TeacherAuthenticationToken 일경우
        TeacherAuthenticationToken token = new TeacherAuthenticationToken();
        if (teacherDB.containsKey(token.getCredentials())){
            return getAuthenticationToken(token.getCredentials());
        }
        return null;
    }

    private TeacherAuthenticationToken getAuthenticationToken(String id) {
        Teacher teacher = teacherDB.get(id);
        return TeacherAuthenticationToken.builder()
                //db에서 찾은 도메인(인증을 통과한 대상)
                .principal(teacher)
                //인증 요청에 대한 정보들 (현재는 임의로 username을 넣음)
                .details(teacher.getUsername())
                //if문을 들어왔으니 db에 존재하기 떄문에 인증이 성공함
                .authenticated(true)
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //UsernamePasswordAuthenticationToken을 제공하는 토큰임을 정의 하는 메서드
       return authentication == TeacherAuthenticationToken.class ||
               authentication == UsernamePasswordAuthenticationToken.class;
    }


    //DB 초기화 작업
    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Teacher("gang","고선생",Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(t -> teacherDB.put(t.getId(),t));
    }
}
