package com.sp.fc.web.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorityTestApplicationTest{

    TestRestTemplate template = new TestRestTemplate("student1","1111");

    @LocalServerPort
    int port;

    public URI uri(String path) {
        try {
            return new URI(format("http://localhost:%d%s", port, path));
        }catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    @DisplayName("컨트롤러 테스트")
    @Test
    void basicTest(){

        String testMessage = template.getForObject(uri("/greeting/ugo"), String.class);
        System.out.println("testMessage = " + testMessage);
        assertEquals("hello ugo!",testMessage);

    }



}