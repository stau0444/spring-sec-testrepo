package com.sp.fc.web.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AuthorityTestApplicationTest extends WebIntegrationTest {

    TestRestTemplate template = new TestRestTemplate("user1","1111");

    @DisplayName("컨트롤러 테스트")
    @Test
    void basicTest(){

        String testMessage = template.getForObject(uri("/greeting"), String.class);
        System.out.println("testMessage = " + testMessage);
        assertEquals("hello there!",testMessage);
    }

}