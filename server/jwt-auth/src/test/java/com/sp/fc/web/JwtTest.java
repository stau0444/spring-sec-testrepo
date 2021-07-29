package com.sp.fc.web;


//import com.auth0.jwt.interfaces.ECDSAKeyProvider;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;


public class JwtTest {
//
//    @DisplayName("OKTA JWT library Test.")
//    @Test
//    void OKTA_JWT_library_Test(){
//
//
//
//        String okta_token = Jwts.builder()
//                .claim("name", "ugo")
//                .claim("price", 1000)
//                .signWith(SignatureAlgorithm.HS256, "ugo")
//                .compact();
//
//        System.out.println("okta_token = " + okta_token);
//        printToken(okta_token);
//
//
//
//    }
//
//    @DisplayName("JAVA- JWT library Test.")
//    @Test
//    void Auth0_JWT_library_Test(){
//
//        byte[] signKey = DatatypeConverter.parseBase64Binary("ugoo");
//
//        String Oauth0 = JWT.create()
//                .withClaim("name", "ugo")
//                .withClaim("price", 1000)
//                .withNotBefore(new Date(System.currentTimeMillis() + 1000))
//                .withExpiresAt(new Date(System.currentTimeMillis() + 2000))
//                .sign(Algorithm.HMAC256(signKey));
//        System.out.println(Oauth0);
//        printToken(Oauth0);
//
//        DecodedJWT jwtToken = JWT.require(Algorithm.HMAC256(signKey)).build().verify(Oauth0);
//        Jws<Claims> jwtsToken = Jwts.parser().setSigningKey(signKey).parseClaimsJws(Oauth0);
//
//        System.out.println("jwtsToken = " + jwtsToken);
//        System.out.println("jwtToken = " + jwtToken);
//    }
//
//    public void printToken(String token){
//        String[] tokens = token.split("\\.");
//        System.out.println("tokens = " + tokens.length);
//        System.out.println("header = " + new String(Base64.getDecoder().decode(tokens[0])));
//        System.out.println("body = " + new String(Base64.getDecoder().decode(tokens[1])));
//        //System.out.println("signature = " + new String(Base64.getDecoder().decode(tokens[2])));
//    }

}
