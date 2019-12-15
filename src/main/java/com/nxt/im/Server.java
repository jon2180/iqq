
package com.nxt.im;

import com.nxt.im.server.NioServer;

import java.io.IOException;

/**
 * 用户程序服务端入口
 * 
 * @version v191204
 */
public final class Server {
  public static void main(String[] args) {
    NioServer server;
    try {
      server = new NioServer(8000);
    } catch (IOException ioE) {
      ioE.printStackTrace();
      System.out.println("in Server");
      return;
    }
    server.listen();
  }
}