package com.nxt.im.common;

import java.nio.ByteBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class DataByteBuffer implements Serializable {

  private static final long serialVersionUID = 1L;

  private String url;

  private Serializable data;

  public DataByteBuffer(ByteBuffer byteBuffer) throws IOException, ClassNotFoundException {
    DataByteBuffer dataByteBuffer = (DataByteBuffer) DataByteBuffer.byteBufferToObject(byteBuffer);
    this.url = dataByteBuffer.url;
    this.data = dataByteBuffer.data;
  }

  public DataByteBuffer(String url, Serializable data) {
    this.url = url;
    this.data = data;
  }

  public String getUrl() {
    return url;
  }

  public Serializable getData() {
    return data;
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