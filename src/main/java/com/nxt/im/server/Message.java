package com.nxt.im.server;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 描述消息的格式信息[尚未完成]
 * @version v191214
 */
public class Message {
  private int code;
  private String message;
  private Object retruns;

  /**
   * str编码成字节缓冲
   * @param str 消息
   * @return ByteBuffer 对象
   */
  public static ByteBuffer encode(String str) {
    return Charset.forName("UTF-8").encode(str);
  }


  Message(){
  }
  Message(int code, String message){
    this.code = code;
    this.message = message;
  }
  Message(int code, String message,Object retruns){
    this.code = code;
    this.message = message;
    this.retruns = retruns;
  }


  // @Override
	// public String toString() {
	// 	return code + "-" + message;
	// }

}
