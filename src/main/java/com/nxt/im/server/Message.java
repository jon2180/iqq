package com.nxt.im.server;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
// import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * 描述消息的格式信息[尚未完成]
 *
 * @version v191214
 */
public class Message {
    /**
     * str编码成字节缓冲
     *
     * @param str 消息
     * @return ByteBuffer 对象
     */
    public static ByteBuffer encode(String str) {
        return StandardCharsets.UTF_8.encode(str);
    }

    public static String getMd5(String str) {

        // MessageDigest md = MessageDigest.getInstance("MD5");

        // // 计算md5函数
        // md.update(str.getBytes());

        // return
        // 保留16位
        // mdPassword = new BigInteger(1, md.digest()).toString(16);
        return str;
    }

    /**
     * 获取随机的7位QQ号
     *
     * @return QQ号
     */
    public static String getRandom() {
        return getRandom(7);
    }

    /**
     * 获取随机的QQ号
     *
     * @param len qq号长度
     * @return QQ号
     */
    public static String getRandom(int len) {
        StringBuilder qq = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < len; ++i) {
            // qq /
            qq.append(random.nextInt(10));
        }

        return qq.toString();
    }
}
