
package com.nxt.im.client;

// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
import java.io.IOException;
// import java.io.InputStream;
// import java.io.ObjectInputStream;
// import java.io.ObjectOutputStream;
// import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
// import java.nio.channels.Channel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

import com.nxt.im.common.Accounts;
import com.nxt.im.common.DataByteBuffer;

// import com.nxt.im.common.Accounts;

/**
 * 基于NIO实现客户端连接的主类
 * 
 * @version v191204
 */
public class Client {

  private InetSocketAddress inetSocketAddress;
  private Selector selector;
  private SocketChannel socketChannel;

  public static void main(String[] args) {
    try {
      Client client = new Client();

      client.init();
      // client.lisent();
    } catch (IOException ioE) {
      ioE.printStackTrace();
    }
  }

  public void init() throws IOException {
    inetSocketAddress = new InetSocketAddress("127.0.0.1", 8000);

    socketChannel = SocketChannel.open(inetSocketAddress);
    socketChannel.configureBlocking(false);

    selector = Selector.open();
    socketChannel.register(selector, SelectionKey.OP_READ);

    /**
     * 接受服务器响应
     */
    Thread thread = new Thread(new ResponseHandler(selector));
    thread.start();
  }

  public void send(String str) throws IOException {
    if (str != null && str.length() > 0) {
      socketChannel.write(Charset.forName("UTF-8").encode(str));
    }
  }

  /**
   * 向服务端发送 byteBuffer消息
   * 
   * @param byteBuffer
   * @throws IOException
   */
  public void send(ByteBuffer byteBuffer) throws IOException {

    if (byteBuffer != null)
      socketChannel.write(byteBuffer);
  }
}