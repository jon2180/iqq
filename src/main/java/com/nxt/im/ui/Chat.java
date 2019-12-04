package com.nxt.im.ui;

import java.io.IOException;

import javax.swing.JFrame;
// import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
// import javax.swing.border.Border;

public class Chat extends JFrame {

  public static final long serialVersionUID = 0x1111111;

  public JPanel chatPanel;

  public Chat() {
    chatPanel = new JPanel();
    chatPanel.setSize(600, 300);

    setTitle("Chat Frame");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JScrollPane jScrollPane = new JScrollPane(chatPanel);
    JScrollPane jTextScrollPane = new JScrollPane(new JTextArea());

    // jScrollPane.add();

    JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jScrollPane, jTextScrollPane);

    add(jSplitPane);
    setVisible(true);
  }

  public void start() {
    sendMessage();

  }

  public void sendMessage() {
    for (int i = 0; i < 7; ++i) {
      try {
        Thread.sleep(1000);
      } catch (Exception ioE) {
        ioE.printStackTrace();
      }
      chatPanel.add(new JLabel("first demo window"));
    }
  }

  public static void main(String[] args) {
    new Chat().start();
  }
}