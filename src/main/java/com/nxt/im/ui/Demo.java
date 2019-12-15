package com.nxt.im.ui;

// import java.io.IOException;

import com.nxt.im.client.*;

public class Demo {

  public static void main(String[] args) {
    if (ClientRouter.userReg("hah", "ainiya")) {
      System.out.println("注册成功");
    } else {
      System.out.println("注册失败");
    }
  }
}
