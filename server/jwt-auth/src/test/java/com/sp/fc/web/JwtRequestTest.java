package com.sp.fc.web;

import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.repository.UserRepository;
import com.sp.fc.user.service.UserService;
import com.sp.fc.web.config.UserLoginForm;
import com.sp.fc.web.test.WebIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class JwtRequestTest extends WebIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @BeforeEach
    void before(){

        userRepository.deleteAll();

        SpUser savedUser = userService.save(SpUser.builder()
                        .email("user1")
                        .password("1111")
                        .enabled(true)
                        .build());
        userService.addAuthority(savedUser.getId(),"ROLE_USER");
    }

    /*
        refresh 토큰을 이용한 로그인 유지 흐름

        1. 클라이언트가 맨처음 로그인 요청을 한다 .
        2. 서버는 로그인 정보로 로그인을 시키고 클라이언트에 auth토큰과 refresh토큰을 내려준다.
        3. 클라이언트는 받은 토큰을 쿠키 또는 로컬스토리지에 저장하고 요청을 요청을 할때마다 auth 토큰을 헤더에 심어서 리소스에 접근한다.
        4. auth 토큰이 만료되면 서버에서는 토큰 만료 에러를 터뜨린다 .
        5. 서버에서 토큰 만료 에러가 돌아오면 클라이언트는 refresh 토큰을 서버로 보낸다 .
        6. 서버에서는 만약 보낸토큰이 refresh 토큰이라면 auth 토큰과 refresh 토큰을 새로 발급해서 넘겨준다
           (클라이언트의 로그인 상태가 갱신되고 , refresh 토큰도 갱신되기 떄문에 로그인이 계속 유지된다.)
        7. 클라이언트는 갱신된 auth 토큰을 통해 리소스에 다시 접근한다 .

     */
    @DisplayName("1. hello 메시지 받아옴")
    @Test
    void test_1(){


        TokenBox token = getToken();

        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        //발급받은 Bearer 토큰을 요청에 넣어줘야한다.
        headers.add(HttpHeaders.AUTHORIZATION,"Bearer "+token.getAuthToken());
        HttpEntity body = new HttpEntity<>(null,headers);
        ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);


    }

    @DisplayName("3. 토큰 만료 테스트")
    @Test
    void test_3() throws InterruptedException {
        //로그인을 해서 auth 토큰을 얻어온 상태
        TokenBox token = getToken();

        //토큰 유효시간이 2초로 설정되있기 떄문에 토큰이 만료된다 .
        Thread.sleep(3000);

        //토큰을 다시 갱신받아서 써야한다.
        RestTemplate client = new RestTemplate();

        //refresh 토큰을 보내서 auth토큰고 refresh토큰을 새로 발급받는다
        TokenBox refreshedToken = refreshToken(token.getRefreshToken());
        //발급받은 Bearer 토큰을 요청에 넣어줘야한다.
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add(HttpHeaders.AUTHORIZATION,"Bearer "+refreshedToken.getAuthToken());
        HttpEntity body2 = new HttpEntity<>(null,headers2);
        ResponseEntity<String> resp3 = client.exchange(uri("/greeting"), HttpMethod.GET, body2, String.class);

        System.out.println("resp3.getBody() = " + resp3.getBody());
        Assertions.assertEquals("hello" , resp3.getBody());

    }
    public TokenBox getToken(){
        RestTemplate client = new RestTemplate();

        HttpEntity<UserLoginForm> body = new HttpEntity<>(
                UserLoginForm.builder().username("user1").password("1111").build()
        );

        //클라이언트가 로그인 요청을 하고 만약 인증이 성공한다면 , 인증한 User를 내려준다.
        ResponseEntity<SpUser> resp = client.exchange(uri("/login"), HttpMethod.POST, body, SpUser.class);

        return  TokenBox.builder()
                .authToken(resp.getHeaders().get("auth_token").get(0))
                .refreshToken(resp.getHeaders().get("refresh_token").get(0))
                .build();
    }

    public TokenBox refreshToken(String refreshToken){
        RestTemplate client = new RestTemplate();


        HttpEntity<UserLoginForm> body = new HttpEntity<>(
                UserLoginForm.builder().refreshToken(refreshToken).build()
        );

        //클라이언트가 가지고 있던 refresh토큰을 서버에 보내서 auth 토큰고 refresh 토큰을 새로 발급받는다 .
        ResponseEntity<SpUser> resp = client.exchange(uri("/login"), HttpMethod.POST, body, SpUser.class);

        //tokenBox는 토큰을 담는 dto이다 .
        //발급받은 토큰을 dto에 담아 리턴한다.
        return  TokenBox.builder()
                .authToken(resp.getHeaders().get("auth_token").get(0))
                .refreshToken(resp.getHeaders().get("refresh_token").get(0))
                .build();
    }
}
