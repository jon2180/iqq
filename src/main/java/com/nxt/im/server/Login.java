package com.nxt.im.server;

public class Login {
  private String waitQnumber; // 待验证用户QQ
  private String waitPassword; // 待验证密码
  private boolean status = false; // 登录状态


  public boolean check(){
    // 查询数据库

    // 进行匹配
    System.out.println("正在验证用户【" + this.waitQnumber + "】是否存在。。。");
    System.out.println("正在验证密码【" + "*******" + "】是否正确。。。");

    // 返回匹配结果 
    return this.status;
  }

  public Login(){
  }

  public Login(String Qnumber, String password){
    this.waitQnumber = Qnumber;
    this.waitPassword = password;
  }


}