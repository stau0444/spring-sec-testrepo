package com.sp.fc.user.repository;


import com.sp.fc.user.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token,String> {

    @Query("select t from Token  t  where t.username=?1 and t.token = ?2")
    Optional<Token> findToken(String username, String token);
}
