package com.nxt.im.client;

import java.io.IOException;

import com.nxt.im.common.Accounts;
import com.nxt.im.common.DataByteBuffer;

public class ClientRouter {

  private static ClientSocket client;

  static {
    try {
      client = new ClientSocket("127.0.0.1", 8000);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 注册
   * 
   * @param nickname 昵称
   * @param password 密码
   * @return
   */
  public static boolean userReg(String nickname, String password) {
    Accounts account = new Accounts();
    account.setNickname(nickname);
    account.setPassword(password);
    // account.setEmail("763653451@qq.com");
    // account.setSignature("this is a signature");
    DataByteBuffer dataByteBuffer = new DataByteBuffer("/user/reg", account);
    dataByteBuffer.setType("json");

    try {
      ClientRouter.client.send(dataByteBuffer.toByteBuffer());
      return true;
    } catch (IOException ioe) {
      ioe.printStackTrace();
      return false;
    }
  }

}