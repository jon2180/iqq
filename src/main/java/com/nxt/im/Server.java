
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
    try {
      new NioServer(8000).listen();
    } catch (IOException ioE) {
      ioE.printStackTrace();
    }
  }
}