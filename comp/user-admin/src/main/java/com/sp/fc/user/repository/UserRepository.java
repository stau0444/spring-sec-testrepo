package com.sp.fc.user.repository;

import com.sp.fc.user.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SpUser,Long> {

    Optional<SpUser> findSpUserByEmail(String email);


}
