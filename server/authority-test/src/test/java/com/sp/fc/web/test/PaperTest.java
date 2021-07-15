package com.sp.fc.web.test;


import com.sp.fc.web.domain.Paper;
import com.sp.fc.web.service.PaperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PaperTest  extends WebIntegrationTest{


    @DisplayName("시험지 리스트 조회")
    @Test
    void papers_test(){
        TestRestTemplate template = new TestRestTemplate("student1","1111");


        ResponseEntity<List<Paper>> resp = template.exchange(uri("/paper/my-papers"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<Paper>>() {
                });

        for (Paper paper : resp.getBody()) {
            System.out.println("paper = " + paper);
        }


        assertEquals(1,resp.getBody().size());
        assertEquals("컴퓨터시험",resp.getBody().get(0).getTitle());



    }
    @DisplayName("student1 이 student2의 시험지는 볼 수 없어야 한다.")
    @Test
    void test2(){

        DefaultWebSecurityExpressionHandler handler1;

        TestRestTemplate template = new TestRestTemplate("student1","1111");

        //현재 접근하는 사람은 student1인데 student2의 시험지를 가져오려 한다.
        ResponseEntity<Paper> resp = template.exchange(uri("/paper/2"), HttpMethod.GET, HttpEntity.EMPTY, Paper.class);

        System.out.println("resp.getBody() = " + resp.getBody());

        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
    }

    @DisplayName("어떤 학생이든 시험 준비중에는 시험지를 볼 수 없다")
    @Test
    void test_3(){
        TestRestTemplate template = new TestRestTemplate("student2","1111");

        //현재 접근하는 사람은 student1인데 student2의 시험지를 가져오려 한다.
        ResponseEntity<Paper> resp = template.exchange(uri("/paper/2"), HttpMethod.GET, HttpEntity.EMPTY, Paper.class);

        System.out.println("resp.getBody() = " + resp.getBody());

        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
    }
    @DisplayName("PostFilter를 통해서 State가 prepare인 오브젝트는 가져 올 수 없다")
    @Test
    void test_4(){
        TestRestTemplate template = new TestRestTemplate("student2","1111");


        ResponseEntity<List<Paper>> resp = template.exchange(uri("/paper/my-papers"), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Paper>>() {});

        System.out.println("resp.getBody() = " + resp.getBody());

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(0 , resp.getBody().size());
    }

    @DisplayName("교장 선생님은 모든 시험지를 확인할 수 있다")
    @Test
    void test_5(){
        TestRestTemplate template = new TestRestTemplate("primary","1111");


        ResponseEntity<List<Paper>> resp = template.exchange(uri("/paper/papers-primary"), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Paper>>() {});

        assertEquals(200,resp.getStatusCodeValue());
        assertEquals(3,resp.getBody().size());

        for (Paper paper : resp.getBody()) {
            System.out.println("paper = " + paper);
        }

    }
}
