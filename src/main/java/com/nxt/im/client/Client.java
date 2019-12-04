
package com.nxt.im.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
// import java.nio.channels.Channel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * 基于NIO实现客户端连接的主类
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

    Thread thread = new Thread(new ResponseHandler(selector));
    thread.start();

  }

  public void send(String str) throws IOException {

    // Scanner scanner = new Scanner(System.in);
    // while (scanner.hasNextLine()) {
    // String request = scanner.nextLine();

    if (str != null && str.length() > 0) {
      socketChannel.write(Charset.forName("UTF-8").encode(str));
    }

    // }

    // scanner.close();
  }

  public void lisent() throws IOException {

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String request = scanner.nextLine();

      if (request != null && request.length() > 0) {
        socketChannel.write(Charset.forName("UTF-8").encode(request));
      }

    }

    scanner.close();
    /**
     * 接受服务器响应
     */

  }

}