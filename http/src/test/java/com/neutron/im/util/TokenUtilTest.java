package com.neutron.im.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

class TokenUtilTest {
//    String str;

    @Test
    public void generateToken() {
//        str = TokenUtil.generateToken("xiaowang");
        var data = new TokenUtil.JwtClaimsData();
        data.setNickname("hello wong");
        var tokenStr = TokenUtil.generateToken(data);
//        System.out.println(str);
        System.out.println(tokenStr);
        var result = TokenUtil.validateToken(tokenStr);
        System.out.println(result.getNickname());
    }

    @Test
//    @Disabled
    public void validateToken() {
        System.out.println(new String(TokenUtil.key.getEncoded(), StandardCharsets.UTF_8));
    }
}
