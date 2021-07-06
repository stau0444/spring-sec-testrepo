package com.sp.fc.web.config;

import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb implements InitializingBean {

    private final UserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!userService.findUser("ugo@ugo.com").isPresent()){
            SpUser user = userService.save(
                    SpUser.builder()
                        .email("ugo@ugo.com")
                        .password("1111")
                        .enabled(true)
                        .build()
                    );
            userService.addAuthority(user.getId(),"ROLE_USER");
        }

        if(!userService.findUser("admin").isPresent()){
            SpUser user = userService.save(
                    SpUser.builder()
                            .email("admin")
                            .password("1111")
                            .enabled(true)
                            .build()
                    );
            userService.addAuthority(user.getId(),"ROLE_ADMIN");
        }

        if(!userService.findUser("user").isPresent()){
            SpUser user = userService.save(
                    SpUser.builder()
                            .email("user")
                            .password("1111")
                            .enabled(true)
                            .build()
                    );
            userService.addAuthority(user.getId(),"ROLE_USER");
        }
    }
}
