package com.sp.fc.web;


import com.sp.fc.web.paper.Paper;
import com.sp.fc.web.paper.PaperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.AclPermissionEvaluator;

@SpringBootTest(classes = AuthorityACLApplication.class ,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaperTest {

    @LocalServerPort
    private int port;

    @Autowired
    PaperRepository paperRepository;

    private String url(long paperId){
        return "http://localhost:"+port+"/paper/"+ paperId;
    }

    @BeforeEach
    void before(){
        paperRepository.deleteAll();

        Paper paper1 = new Paper(1L,"paper1","tutor1",Paper.State.PREPARE);

        paperRepository.save(paper1);
    }

    @DisplayName("1.시험지 가져오기")
    @Test
    void test_1(){

        TestRestTemplate template = new TestRestTemplate("student1","1111");
        ResponseEntity<Paper> resp = template.getForEntity(url(1L), Paper.class);

        System.out.println("resp.getBody() = " + resp.getBody());
    }
}
