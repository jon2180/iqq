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

  /**
   * 登录
   * 
   * @param qnumber 昵称
   * @param password 密码
   * @return
   */
  public static boolean userLog(String qnumber, String password) {
    Accounts account = new Accounts();
    account.setQnumber(qnumber);
    account.setPassword(password);

    DataByteBuffer dataByteBuffer = new DataByteBuffer("/user/login", account);
    dataByteBuffer.setType("json");

    try {
      ClientRouter.client.send(dataByteBuffer.toByteBuffer());
      return true;
    } catch (IOException ioe) {
      ioe.printStackTrace();
      return false;
    }
  }


  /**
   * TODO 关于线程间通信的想法
   * 
   * 创建另一个线程用来监测通信的过程，在里面创建一张通信记录表，类似于{线程id + id : value}
   * 调用userReg、userLog方法的时候到一个表里去注册一个id
   * 然后在ResponseHandler拿到返回的信息，（打印到控制台的那个地方），存到这个id的value里
   * 然后监测进程死循环取出信息来分发给相应的线程？
   */

}