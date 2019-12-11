package com.nxt.im.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.nxt.im.common.Accounts;
import com.nxt.im.common.DataByteBuffer;

public class Register {

  public static void main(String[] args) {
    try {
      new Register().lisent();
    } catch (IOException ioE) {
      ioE.printStackTrace();
    }
  }
  // private Accounts account;

  public void lisent() throws IOException {
    try {
      Client client = new Client();
      client.init();

      Accounts account = new Accounts();
      account.setNickname("my");
      account.setEmail("763653451@qq.com");
      account.setSignature("this is a signature");

      DataByteBuffer dataByteBuffer = new DataByteBuffer("/user/reg", account);
      client.send(dataByteBuffer.toByteBuffer());
      // send(new DataByteBuffer("/user/name", account).toByteBuffer());

      DataByteBuffer buffer = new DataByteBuffer(dataByteBuffer.toByteBuffer());

      System.out.println(buffer.getUrl());
      Accounts trans = (Accounts) buffer.getData();

      // ByteBuffer byteBuffer = DataByteBuffer.objectToByteBuffer(account);
      // Accounts trans = (Accounts) DataByteBuffer.byteBufferToObject(byteBuffer);
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

  public Register() {
  }

}