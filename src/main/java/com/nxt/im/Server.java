
package com.nxt.im;

import com.nxt.im.server.NioServer;

import java.io.IOException;


/**
 * 用户程序服务端入口
 * @version v191204
 */
public class Server {
  public static void main(String[] args) {
    try {
      new NioServer(8000).start();
    } catch (IOException ioE) {
      ioE.printStackTrace();
    }
  }
}