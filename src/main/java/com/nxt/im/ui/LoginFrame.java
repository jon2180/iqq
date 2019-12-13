package com.nxt.im.ui;
import java.awt.*;
// import java.awt.FlowLayout;
// import java.awt.GridLayout;
import com.nxt.im.client.Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*; 

public class LoginFrame extends JFrame{//类名 loginFrame
    
    private JFrame loginFrame = new JFrame("登录");
    private Container lc = loginFrame.getContentPane();
    private JLabel a1 = new JLabel("用户名");
    private JTextField username = new JTextField();
    private JLabel a2 = new JLabel("密   码");
    private JPasswordField password = new JPasswordField();
    private JButton certainbtn = new JButton("确定");
    private JButton cancelbtn = new JButton("取消");
    private JButton registerbtn = new JButton("注册");

    public LoginFrame() {
        //设置窗体的位置及大小
        loginFrame.setBounds(600, 200, 300, 220);
        // registerFrame.setBounds(700, 300, 400, 300);
        //设置一层相当于桌布的东西
        // rc.setLayout(new BorderLayout());
        lc.setLayout(new BorderLayout());//布局管理器
        //设置按下右上角X号后关闭
        // registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //初始化--往窗体里放其他控件
        init();
        //设置窗体可见
        loginFrame.setVisible(true);
    }
    public void init() {
        /*标题部分--North*/
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(new JLabel("fakeque"));
       // rc.add(titlePanel, "North");
        lc.add(titlePanel, "North");
        
        /*输入部分--Center*/
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);
        a1.setBounds(50, 20, 50, 20);
        a2.setBounds(50, 60, 50, 20);
        fieldPanel.add(a1);
        fieldPanel.add(a2);
        username.setBounds(110, 20, 120, 20);
        password.setBounds(110, 60, 120, 20);
        fieldPanel.add(username);
        fieldPanel.add(password);
        lc.add(fieldPanel, "Center");    

        /*按钮部分--South*/
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new FlowLayout());
        buttonPanel1.add(certainbtn);
        buttonPanel1.add(cancelbtn);
        buttonPanel1.add(registerbtn);
        lc.add(buttonPanel1, "South");

        //创建监听器对象
		ActionListener lar = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                loginFrame.setVisible(false);
                // registerFrame.setVisible(true);
                RegisterFrame rf = new RegisterFrame(); // 创建注册窗体
			}
		};
		//把监听器注册到控件上
		registerbtn.addActionListener(lar);
		setSize(900,600);
        
        ActionListener lacer = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                String uname = username.getText();
                String pwd = String.valueOf(password.getPassword());

                try {
                    Login loginObj = new Login(uname,pwd);
                    loginObj.lisent();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                
                System.out.println(uname+ " " +pwd);
			}
		};
		//把监听器注册到控件上
		certainbtn.addActionListener(lacer);
		setSize(900,600);

        ActionListener lacan = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                password.setText("");
                username.setText("");
			}
		};
		//把监听器注册到控件上
		cancelbtn.addActionListener(lacan);
		setSize(900,600);

    }
}
