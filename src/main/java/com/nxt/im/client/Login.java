package com.nxt.im.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.nxt.im.common.Accounts;
import com.nxt.im.common.DataByteBuffer;

public class Login {

  private String nickname;
  private String password;

  // public static void main(String[] args) {
  //   // try {
  //   //   // new Login().lisent();
  //   // } catch (IOException ioE) {
  //   //   ioE.printStackTrace();
  //   // }
  // }
  // private Accounts account;

  public void lisent() throws IOException {
    try {
      ClientSocket client = new ClientSocket();
      // client.init();

      Accounts account = new Accounts();
      account.setNickname(this.nickname);
      account.setPassword(this.password);

      DataByteBuffer dataByteBuffer = new DataByteBuffer("/user/login", account);
      client.send(dataByteBuffer.toByteBuffer());

      DataByteBuffer buffer = new DataByteBuffer(dataByteBuffer.toByteBuffer());

      System.out.println(buffer.getUrl());
      Accounts trans = (Accounts) buffer.getData();

      System.out.println(trans.getSignature());
    } catch (IOException ioe) {
      // ioe.printStackTrace();
      return;
    } catch (ClassNotFoundException cnfE) {
      cnfE.printStackTrace();
      return;
    }
    /**
     * 接受服务器响应
     */

  }

  public Login() {

  }
  public Login(String nickname, String password) {
    this.nickname = nickname;
    this.password = password;
  }


}