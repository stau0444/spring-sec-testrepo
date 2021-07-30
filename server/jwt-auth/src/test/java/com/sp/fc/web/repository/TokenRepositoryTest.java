package com.sp.fc.web.repository;

import com.sp.fc.user.repository.TokenRepository;
import com.sp.fc.user.domain.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    TokenRepository tokenRepository;

    @DisplayName("1.토큰 저장 테스트")
    @Test
    void test_1(){
        Token token = Token.builder()
                .username("user")
                .token("test-token")
                .build();

        tokenRepository.save(token);

        List<Token> all = tokenRepository.findAll();
        for (Token tokens : all) {
            System.out.println("repository = " + token);
        }

    }
}