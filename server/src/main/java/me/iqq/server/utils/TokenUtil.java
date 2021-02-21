package me.iqq.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class TokenUtil {

    /**
     * jsonwebtoken 假实现
     */
    private static final String originalToken = "aaaaaaa.bbbbbb.cccccc";

    // The JWT signature algorithm we will be using to sign the token SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    // We will sign our JWT with our ApiKey secret
    private static final SecretKey apiKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

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

    private static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setSubject(subject)
            .setIssuer(issuer)
            .signWith(apiKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = System.currentTimeMillis() + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    // Sample method to validate and read the JWT
    private static void parseJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parserBuilder()
//            .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
            .setSigningKey(new SecretKeySpec(originalToken.getBytes(), SignatureAlgorithm.HS256.getJcaName()))
            .build()
            .parseClaimsJws(jwt).getBody();
        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration());
    }
}

