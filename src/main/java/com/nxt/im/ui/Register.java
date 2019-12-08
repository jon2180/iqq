package com.nxt.im.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.nxt.im.db.Accounts;

public class Register extends JFrame {
  private Accounts account;
  private JLabel labelBg; // 背景颜色

  public Register() {

  }

  public Register(Accounts account) {
    this.account = account;
  }

  
}