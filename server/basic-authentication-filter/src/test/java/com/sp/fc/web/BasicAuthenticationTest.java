package com.sp.fc.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationTest {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate();

    private String greetingUrl(){
        return "http://localhost:"+port+"/greeting";
    }


    @DisplayName("1 인증 실패")
    @Test
    void greetingTest(){
        //헤더를 안 실어 보냈기 떄문에 HttpClientErrorException가 발생할 것이다.
        HttpClientErrorException exception = Assertions.assertThrows(HttpClientErrorException.class, ()->{
                client.getForObject(greetingUrl(), String.class);
        });

        //401 error 발생
        assertEquals(401 , exception.getRawStatusCode());
    }

    @DisplayName("2 인증 성공")
    @Test
    void success(){
        //헤더를 만들어서
        HttpHeaders headers = new HttpHeaders();
        //헤더에 정보 추가
        headers.add(HttpHeaders.AUTHORIZATION ,"Basic "+ Base64.getEncoder().encodeToString(
                "user1:1111".getBytes()
        ));

        //헤더 출력
        System.out.println("headers = " + headers);

        //Http 요청 정보를 담는 entity 생성
        HttpEntity entity = new HttpEntity(null,headers);

        //restTemplate의 exchange 메서드로 요청
        // 파라미터는 첫번째 부터 요청 URL , 요청메서드 , 요정 정보 엔티티 , 반환타입
        ResponseEntity<String> resp = client.exchange(greetingUrl(), HttpMethod.GET, entity , String.class);

        assertEquals("hello", resp.getBody());

    }

    @DisplayName("3 testRestTemplate 활용")
    @Test
    void useTestRestTemplate(){
        //테스트용 restTemplate
        //파라미터로 username 과 password를 넘기면
        //알아서 Authorization 헤더를 만들어준다
        TestRestTemplate template = new TestRestTemplate("user1","1111" );

        String resp = template.getForObject(greetingUrl(), String.class);

        assertEquals(resp , "hello");
    }

    @DisplayName("4  post request Test")
    @Test
    void  postRequest(){

        TestRestTemplate template = new TestRestTemplate("user1","1111" );

        ResponseEntity<String> resp = template.postForEntity(greetingUrl(), "ugo", String.class);

        assertEquals("hello ugo" , resp.getBody());
    }
}
