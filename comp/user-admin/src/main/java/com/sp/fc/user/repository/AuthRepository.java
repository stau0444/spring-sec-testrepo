package com.sp.fc.user.repository;

import com.sp.fc.user.domain.SpAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<SpAuthority,Long> {

    SpAuthority findByAuthorityAndAndUserId(String authroity,Long userId);
}
