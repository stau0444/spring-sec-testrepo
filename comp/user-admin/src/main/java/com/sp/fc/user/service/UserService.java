package com.sp.fc.user.service;

import com.sp.fc.user.domain.SpAuthority;
import com.sp.fc.user.domain.SpOAuth2User;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.repository.SpOAuth2UserRepository;
import com.sp.fc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final SpOAuth2UserRepository oAuth2UserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findSpUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<SpUser> findUser(String email){
        return userRepository.findSpUserByEmail(email);
    }

    public SpUser save(SpUser user){
        return userRepository.save(user);
    }

    public void addAuthority(Long userId,String authority){
        userRepository.findById(userId).ifPresent(user -> {
            SpAuthority newRole = new SpAuthority(user.getId(),authority);
            if(user.getAuthorities() == null){
                HashSet<SpAuthority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }else if(!user.getAuthorities().contains(newRole)){
                HashSet<SpAuthority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }
        });
    }

    public void addAuthority(Long userId, List<String> authorities){
        userRepository.findById(userId).ifPresent(user -> {

            HashSet<SpAuthority> authList = new HashSet<>();

                for (String authority : authorities) {
                    SpAuthority newRole = new SpAuthority(user.getId(),authority);
                    authList.add(newRole);
                }
            user.setAuthorities(authList);
            save(user);

        });
    }

    public void removeAuthority(Long userid , String authority){
        userRepository.findById(userid).ifPresent(
                user -> {
                    if(user.getAuthorities() == null){
                        return;
                    }
                    SpAuthority targetRole = new SpAuthority(userid, authority);
                    if(user.getAuthorities().contains(targetRole)){
                        user.setAuthorities(
                                user.getAuthorities().stream().filter(
                                        auth -> !(auth.equals(targetRole)))
                                .collect(Collectors.toSet())
                        );
                        save(user);
                    }
                }
        );
    }

    //OAuth2User 정보를 줬을때 spUser 정보를 로딩시켜줌
    public SpUser load(SpOAuth2User oAuth2User){
        SpOAuth2User dbUser = oAuth2UserRepository.findById(oAuth2User.getOauth2UserId())
                .orElseGet(()->{
                    SpUser user = new SpUser();
                    user.setEmail(oAuth2User.getEmail());
                    user.setName(oAuth2User.getName());
                    user.setEnabled(true);
                    user = userRepository.save(user);
                    addAuthority(user.getId(),"ROLE_USER");

                    oAuth2User.setUserId(user.getId());
                    return oAuth2UserRepository.save(oAuth2User);
                });
        return userRepository.findById(dbUser.getUserId()).get();
    }
}
