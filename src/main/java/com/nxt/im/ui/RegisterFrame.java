package com.nxt.im.ui;
import java.awt.*;
// import java.awt.FlowLayout;
// import java.awt.GridLayout;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

//package 
public class RegisterFrame extends JFrame{//类名 loginFrame
    
    private JFrame registerFrame;
    private Container rc;
    private JLabel a1;
    private JTextField username;
    private JLabel a2;
    private JPasswordField password;
    private JPasswordField repassword;
    private JLabel a3;
    private JButton certainbtn;
    private JButton cancelbtn;

    RegisterFrame() {
        this.registerFrame = new JFrame("注册");
        this.rc = registerFrame.getContentPane();
        this.a1 = new JLabel("用户名");
        this.username = new JTextField();
        this.a2 = new JLabel("密   码");
        this.password = new JPasswordField();
        this.repassword = new JPasswordField();
        this.a3 = new JLabel("确认密码");
        this.certainbtn = new JButton("确定");
        this.cancelbtn = new JButton("取消");

        //设置窗体的位置及大小
        registerFrame.setBounds(700, 300, 400, 300);
        //设置一层相当于桌布的东西
        rc.setLayout(new BorderLayout());//布局管理器
        //设置按下右上角X号后关闭
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗体可见
        registerFrame.setVisible(true);
        //初始化--往窗体里放其他控件
        init();

        System.out.println("已执行完毕init");
    }

    public void init() {
        /*标题部分--North*/
        JPanel titlePanel1 = new JPanel();
        titlePanel1.setLayout(new FlowLayout());
        titlePanel1.add(new JLabel("fakeque"));
        rc.add(titlePanel1, "North");

        /*输入部分--Center*/
        JPanel fieldPanel1 = new JPanel();
        fieldPanel1.setLayout(null);
        a1.setBounds(50, 20, 50, 20);
        a2.setBounds(50, 60, 50, 20);
        a3.setBounds(50, 100, 50, 20);
        fieldPanel1.add(a1);
        fieldPanel1.add(a2);
        fieldPanel1.add(a3);
        username.setBounds(110, 20, 120, 20);
        password.setBounds(110, 60, 120, 20);
        repassword.setBounds(110, 100, 120, 20);
        fieldPanel1.add(username);
        fieldPanel1.add(password);
        fieldPanel1.add(repassword);
        rc.add(fieldPanel1, "Center");

        /*按钮部分--South*/
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(certainbtn);
        buttonPanel.add(cancelbtn);
        rc.add(buttonPanel, "South");

        //创建 确定按钮 监听器对象
		ActionListener rar = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                registerFrame.setVisible(false);
                LoginFrame ft = new LoginFrame(); // 创建登录窗体
			}
        };
        certainbtn.addActionListener(rar);

        //创建 取消按钮 监听器对象
        ActionListener racan = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                username.setText("");
                password.setText("");
                repassword.setText("");
			}
		};
		//把监听器注册到控件上
		cancelbtn.addActionListener(racan);

    }
    // 测试
    // public static void main(String[] args) {
    //     new RegisterFrame();
    // }
}
