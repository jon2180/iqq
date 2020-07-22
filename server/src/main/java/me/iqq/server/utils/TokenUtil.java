package me.iqq.server.utils;

//import javax.crypto.spec.SecretKeySpec;
//import javax.xml.bind.DatatypeConverter;
//import java.security.Key;
//import java.util.Date;

//import io.jsonwebtoken.*;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.Claims;

public class TokenUtil {

    /**
     * jsonwebtoken 假实现
     */
    private static final String originalToken = "aaaaaaa.bbbbbb.cccccc";

    /**
     * 签发 token
     *
     * @return 生成的 token 字符串
     */
    public static String generate() {
        return originalToken;
    }

    /**
     * 验证 token
     *
     * @param token token 字符串
     * @return 验证结果
     */
    public static boolean verify(String token) {
        return token.equals(originalToken);
    }
//
//    private String createJWT(String id, String issuer, String subject, long ttlMillis) {
//
////The JWT signature algorithm we will be using to sign the token
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//
////We will sign our JWT with our ApiKey secret
//        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey.getSecret());
//        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
//        //Let's set the JWT Claims
//        JwtBuilder builder = Jwts.builder().setId(id)
//            .setIssuedAt(now)
//            .setSubject(subject)
//            .setIssuer(issuer)
//            .signWith(signatureAlgorithm, signingKey);
//
////if it has been specified, let's add the expiration
//        if (ttlMillis >= 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }
//
////Builds the JWT and serializes it to a compact, URL-safe string
//        return builder.compact();
//    }
//
//    //Sample method to validate and read the JWT
//    private void parseJWT(String jwt) {
////This line will throw an exception if it is not a signed JWS (as expected)
//        Claims claims = Jwts.parser()
//            .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey.getSecret()))
//            .parseClaimsJws(jwt).getBody();
//        System.out.println("ID: " + claims.getId());
//        System.out.println("Subject: " + claims.getSubject());
//        System.out.println("Issuer: " + claims.getIssuer());
//        System.out.println("Expiration: " + claims.getExpiration());
//    }

}

