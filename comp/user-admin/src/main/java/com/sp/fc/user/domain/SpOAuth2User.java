package com.sp.fc.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;

import static java.lang.String.format;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sp_oauth2_user")
public class SpOAuth2User {

    public  static enum OAuth2Provider{
        google{
            public SpOAuth2User convert(OAuth2User user){
                return SpOAuth2User.builder()
                        .oauth2UserId(format("%s_%s", name(), user.getAttribute("sub")))
                        .provider(google)
                        .email(user.getAttribute("email"))
                        .name(user.getAttribute("name"))
                        .created(LocalDateTime.now())
                        .build();
            }
        },
        naver{
            public SpOAuth2User convert(OAuth2User user){
                Map<String, Object> resp = user.getAttribute("response");
                return SpOAuth2User.builder()
                        .oauth2UserId(format("%s_%s", name(), resp.get("id")))
                        .provider(naver)
                        .email(""+resp.get("email"))
                        .name(""+resp.get("name"))
                        .created(LocalDateTime.now())
                        .build();
            }
        };
        public abstract SpOAuth2User convert(OAuth2User userInfo);
    }

    //인증 제공서버에서의 유저 id
    @Id
    private String oauth2UserId;

    //로컬의 User id;
    private Long userId;

    private String name;
    private String email;
    private LocalDateTime created;

    private OAuth2Provider provider;



}
