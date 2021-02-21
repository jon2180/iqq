package com.neutron.im.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xxm
 * @description:
 * @date: 2020/5/28 15:53
 */
@Data
public class TokenUtil {
    /**
     * 过期时间---24 hour
     */
    private static final int EXPIRATION_TIME = 60 * 60 * 24;
    /**
     * 自己设定的秘钥
     */
//    private static final String SECRET = "023bdc63c3c5a4587*9ee6581508b9d03ad39a74fc0c9a9cce604743367c9646b";
    /**
     * 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * 表头授权
     */
//    public static final String AUTHORIZATION = "Authorization";

    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(JwtClaimsData data) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        // 设置签发时间
        calendar.setTime(new Date());
        // 设置过期时间
        // 添加秒钟
        calendar.add(Calendar.SECOND, EXPIRATION_TIME);
        Date time = calendar.getTime();
        String jwt = Jwts.builder()
                .setClaims(data.toHaspMap())
                //签发时间
                .setIssuedAt(now)
                //过期时间
                .setExpiration(time)
                .signWith(key)
//                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        //jwt前面一般都会加Bearer
        return TOKEN_PREFIX + jwt;
    }

    /**
     * 解密Token
     * 2020/5/28 16:18
     *
     * @param token token 字符串，应该以 'Bearer ' 开头:
     * @return 解析出来的字符串
     * @throws JwtException Jwt 解析出错，可能是 jwt 不合法，jwt 过期 等原因
     */
    public static JwtClaimsData validateToken(String token) throws JwtException {
        // parse the token.
        Map<String, Object> body = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();


        return JwtClaimsData.dataFor(body); // .get("userName").toString();
    }

    @Data
    public static class JwtClaimsData {
        private String uid;
        private String nickname;
        private String email;

        public Map<String, String> toHaspMap() {
            return new HashMap<>(){{
                put("uid", uid);
                put("nickname", nickname);
                put("email", email);
            }};
        }

        public static JwtClaimsData dataFor(Map<String, Object> map) {
            return new JwtClaimsData() {{
                setUid((String) map.getOrDefault("uid", ""));
                setEmail((String) map.getOrDefault("email", ""));
                setNickname((String) map.getOrDefault("nickname", ""));
            }};
        }
    }

}
