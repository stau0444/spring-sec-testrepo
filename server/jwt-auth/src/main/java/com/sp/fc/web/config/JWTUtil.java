package com.sp.fc.web.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sp.fc.user.domain.SpUser;

import java.time.Instant;

//JWT 발행 해주는 Util class
public class JWTUtil {

    private static final Algorithm ALGORITHM =Algorithm.HMAC256("ugogo");
    private static final long AUTH_TIME = 20 * 60 ;
    private static final long REFRESH_TIME = 60 * 60 *24 * 7 ;

    public static String createAuthToken(SpUser user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond() + AUTH_TIME)
                .sign(ALGORITHM);
    }

    public static String createRefreshToken(SpUser user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static VerifyResult verify(String token){
        try {
            //인증 성공시
            DecodedJWT result = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder()
                    .success(true)
                    .username(result.getSubject())
                    .build();
        }catch (Exception e){
            //인증 실패시
            DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder()
                    .success(false)
                    .username(decode.getSubject())
                    .build();
        }
    }
}
