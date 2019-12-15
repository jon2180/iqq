package com.nxt.im.ui;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

public class Chat extends JFrame {

    private JFrame chatFrame;
    private Container cc;
    private JButton sendbtn;
    private JTextArea jta;
    private JTextField jtf;
    private JTextPane msgArea;
    private JScrollPane textScrollPane;
    private JScrollBar vertical;
    private JTextArea jtashow;

    Chat() {
        this.chatFrame = new JFrame("chat room");
        this.cc = chatFrame.getContentPane();
        this.sendbtn = new JButton("发送");
        this.jta = new JTextArea(15, 35);
        this.jtf = new JTextField(15);
        this.msgArea = new JTextPane();
        this.textScrollPane = new JScrollPane();
        this.vertical = new JScrollBar(JScrollBar.VERTICAL);
        this.jtashow = new JTextArea(20, 35);

        // 设置窗体的位置及大小
        chatFrame.setBounds(900, 500, 600, 500);
        // 设置一层相当于桌布的东西
        cc.setLayout(new BorderLayout());// 布局管理器
        // 设置按下右上角X号后关闭
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗体可见
        chatFrame.setVisible(true);
        // 初始化--往窗体里放其他控件

        init();
    }

    public void init() {
        /* 标题部分--North */
        JPanel titlePanel1 = new JPanel();
        titlePanel1.setLayout(new FlowLayout());
        titlePanel1.add(new JLabel("fakeque"));
        cc.add(titlePanel1, "North");

        /* 输入部分--Center */
        JPanel fieldPanel = new JPanel();
        // fieldPanel.setLayout(null);
        fieldPanel.add(jtf);
        // msgArea.setEditable(false);
        // textScrollPane.setViewportView(msgArea);
        // vertical.setAutoscrolls(true);
        // textScrollPane.setVerticalScrollBar(vertical);
        // fieldPanel.add(msgArea);
        cc.add(fieldPanel);

        JPanel textPanel = new JPanel();
        jtashow.setEditable(false);
        textPanel.add(jtashow);
        textPanel.add(jta);
        cc.add(textPanel);

        // JPanel textPanel = new JPanel();
        // textPanel.add(jta,"Center");
        /* 按钮部分--South */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(sendbtn);
        cc.add(buttonPanel, "South");

        // 创建 send 监听器对象
        ActionListener ca = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = jta.getText();
                String uname = "username";
                String time = "time+";
                System.out.println(content);
                jtashow.append(time + uname + ":\r\n" + content + "\r\n");
                jtashow.setLineWrap(true);
                jta.setText("");
            }
        };
        sendbtn.addActionListener(ca);
    }
}
