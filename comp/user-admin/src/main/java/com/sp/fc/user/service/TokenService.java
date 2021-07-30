package com.sp.fc.user.service;

import com.sp.fc.user.domain.Token;
import com.sp.fc.user.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository repository;

    public void saveToken(Token token){
        repository.save(token);
    }

    public Optional<Token> findToken(String username, String refreshToken) {
        return repository.findToken(username,refreshToken);
    }

    public void deleteAll(String username) {
        repository.deleteById(username);
    }
}
