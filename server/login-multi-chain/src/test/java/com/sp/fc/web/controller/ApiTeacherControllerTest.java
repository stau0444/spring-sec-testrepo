package com.sp.fc.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.fc.web.student.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.propertyeditors.ClassEditor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTeacherControllerTest {

    @LocalServerPort
    private int port;

    public String getUrl(){
        return "http://localhost:"+port+"/api/teacher/students";
    }


    @DisplayName("1 학생 목록 조회")
    @Test
    void searchStudentList() throws JsonProcessingException {
        String url = String.format("http://localhost:%d/api/teacher/students", port);
        TestRestTemplate testClient = new TestRestTemplate("gang", "11");
        ResponseEntity<List<Student>> resp = testClient.exchange("http://localhost:" + port + "/api/teacher/students",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                });
        System.out.println("body= "+ resp.getBody());
        assertEquals(3,resp.getBody().size());
    }

}