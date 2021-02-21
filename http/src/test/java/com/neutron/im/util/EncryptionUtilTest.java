package com.neutron.im.util;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilTest {

    @Test
    void md5() {
        try {
            System.out.println(EncryptionUtil.md5("test"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    void sha1() {
//    }
//
//    @Test
//    void sha256() {
//    }
//
//    @Test
//    void hmacSha1() {
//    }
//
//    @Test
//    void testHmacSha1() {
//    }
//
//    @Test
//    void createHmacSha1Key() {
//    }
//
//    @Test
//    void hmacSha256() {
//    }
//
//    @Test
//    void testHmacSha256() {
//    }
//
//    @Test
//    void createHmacSha256Key() {
//    }
}
