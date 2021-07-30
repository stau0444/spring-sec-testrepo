package com.sp.fc.web.config;

import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDB implements InitializingBean {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @Override
    public void afterPropertiesSet() throws Exception {
        userService.save(SpUser.builder()
                .email("user1")
                .password("1111")
                .enabled(true)
                .build());
    }
}
