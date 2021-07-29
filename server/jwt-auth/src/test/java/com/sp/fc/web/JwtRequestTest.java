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

    @DisplayName("1. hello 메시지 받아옴")
    @Test
    void test_1(){

        RestTemplate client = new RestTemplate();

        HttpEntity<UserLoginForm> body = new HttpEntity<>(
                UserLoginForm.builder().username("user1").password("1111").build()
        );

        //클라이언트가 로그인 요청을 하고 만약 인증이 성공한다면 , 인증한 User를 내려준다.
        ResponseEntity<SpUser> resp = client.exchange(uri("/login"), HttpMethod.POST, body, SpUser.class);


        System.out.println(
                "resp.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0) = "
                        + resp.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0)
        );
        System.out.println("resp.getBody() = " + resp.getBody());
        Assertions.assertEquals("user1",resp.getBody().getUsername());

        HttpHeaders headers = new HttpHeaders();
        //발급받은 Bearer 토큰을 요청에 넣어줘야한다.
        headers.add(HttpHeaders.AUTHORIZATION,resp.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
        body = new HttpEntity<>(null,headers);
        ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);

        System.out.println("resp2 = " + resp2);
    }
}
