package com.nxt.im.common;

import java.nio.ByteBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;


/**
 * DataByteBuffer
 * @description 用于在服务端与客户端通道 Channel 中传递的消息格式规定
 * @version 191215
 * @serial
 */
public class DataByteBuffer implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Message id
   * 
   * @description 指定唯一的消息id
   */
  private int mid;

  /**
   * @description 用于接收方收到消息后的操作
   */
  private String url;

  /**
   * @description 真实数据
   */
  private Serializable data;

  /**
   * @description 消息类型 - 类似于 http 中的 Content-Type 字段
   */
  private String type = "serial-data";

  /**
   * @description 消息建立的时间戳
   */
  private long time;

  /**
   * @description 状态码 类似于 http 的状态码
   */
  private int statusCode;

  public DataByteBuffer(ByteBuffer byteBuffer) throws IOException, ClassNotFoundException {
    DataByteBuffer dataByteBuffer = (DataByteBuffer) DataByteBuffer.byteBufferToObject(byteBuffer);
    this.url = dataByteBuffer.url;
    this.data = dataByteBuffer.data;
    this.type = dataByteBuffer.type;
    this.time = System.currentTimeMillis();
  }

  public DataByteBuffer(String url, Serializable data) {
    this.url = url;
    this.data = data;
    this.type = "serial-data";
    this.time = System.currentTimeMillis();
  }

  public DataByteBuffer() {

  }

  /**
   * @return the mid
   */
  public int getMid() {
    return mid;
  }
  
  /**
   * @param statusCode the statusCode to set
   */
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * @return the statusCode
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  /**
   * @return the time
   */
  public long getTime() {
    return time;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  public Serializable getData() {
    return data;
  }

  /**
   * @param data the data to set
   */
  public void setData(Serializable data) {
    this.data = data;
  }

  public ByteBuffer toByteBuffer() throws IOException {
    return DataByteBuffer.objectToByteBuffer(this);
  }

  /**
   * 对象序列化，转为 ByteBuffer
   * 
   * @param obj
   * @return
   * @throws IOException
   */
  public static ByteBuffer objectToByteBuffer(Serializable obj) throws IOException {
    // try {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bout);
    out.writeObject(obj);
    out.flush();
    byte[] bytes = bout.toByteArray();
    bout.close();
    out.close();
    // ————————————————
    // 版权声明：本文为CSDN博主「eclipser1987」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
    // 原文链接：https://blog.csdn.net/eclipser1987/article/details/5350007
    return ByteBuffer.wrap(bytes);
  }

  /**
   * ByteBuffer 还原为 Object
   * 
   * @param buffer
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static Object byteBufferToObject(ByteBuffer buffer) throws IOException, ClassNotFoundException {
    InputStream input = new ByteArrayInputStream(buffer.array());
    ObjectInputStream oi = new ObjectInputStream(input);
    Object obj = oi.readObject();
    input.close();
    oi.close();
    buffer.clear();
    return obj;
    // ————————————————
    // 版权声明：本文为CSDN博主「eclipser1987」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
    // 原文链接：https://blog.csdn.net/eclipser1987/article/details/5350007
  }
}