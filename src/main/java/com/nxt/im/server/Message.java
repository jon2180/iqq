package com.nxt.im.server;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 描述消息的格式信息[尚未完成]
 * @version v191204
 */
public class Message {

  /**
   * str编码成字节缓冲
   * @param str 消息
   * @return ByteBuffer 对象
   */
  public static ByteBuffer encode(String str) {
    return Charset.forName("UTF-8").encode(str);
  }

}
