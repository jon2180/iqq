package me.iqq.client.ui;

import me.iqq.client.controller.RequestUtils;
import me.iqq.client.model.FramesManager;
import me.iqq.common.protocol.Friend;

import javax.swing.*;
import java.util.Vector;

//类名 loginFrame
public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static volatile LoginFrame instance = null;
    //
//    public static LoginFrame getInstance() {
//        if (instance == null) {
//            synchronized (RegisterFrame.class) {
//                instance = new LoginFrame();
//            }
//        }
//        return instance;
//    }
    private FramesManager framesManager;

    public LoginFrame(FramesManager fm) {
        super("QQ");

        framesManager = fm;

        //设置窗体的位置及大小
        setBounds(600, 200, 300, 220);

        //设置一层相当于桌布的东西
        // rc.setLayout(new BorderLayout());

//        loginFrame.getContentPane().setLayout(new BorderLayout());//布局管理器
        //设置按下右上角X号后关闭
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //初始化--往窗体里放其他控件

        // 标题部分--North
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("登录"));
        getContentPane().add(titlePanel, "North");

        // 输入部分--Center
        JPanel fieldPanel = new JPanel();

        // QQ 号
        JLabel a1 = new JLabel("QQ号");
        JTextField qnumber = new JTextField();
        a1.setBounds(50, 20, 50, 20);
        fieldPanel.add(a1);
        qnumber.setBounds(110, 20, 120, 20);
        fieldPanel.add(qnumber);

        // 密码
        JLabel a2 = new JLabel("密   码");
        JPasswordField password = new JPasswordField();
        a2.setBounds(50, 60, 50, 20);
        fieldPanel.add(a2);
        password.setBounds(110, 60, 120, 20);
        fieldPanel.add(password);

        fieldPanel.setLayout(null);
        getContentPane().add(fieldPanel, "Center");

        // 按钮部分--South
        JPanel buttonPanel1 = new JPanel();

        JButton certainBtn = new JButton("确定");
        //把监听器注册到控件上
        certainBtn.addActionListener(e -> {
            String qnum = qnumber.getText();
            String pwd = String.valueOf(password.getPassword());
            Object message = "wrong username or password!";
            String title = "ERROR";
            if (qnum.equals("") || pwd.equals("")) {
                System.out.println("QQ号或密码不能为空");
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
            } else {

                RequestUtils.login(qnum, pwd);

            }

        });
        buttonPanel1.add(certainBtn);

        JButton cancelBtn = new JButton("重置");
        //把监听器注册到控件上
        cancelBtn.addActionListener(e -> {
            password.setText("");
            qnumber.setText("");
        });
        buttonPanel1.add(cancelBtn);

        JButton registerBtn = new JButton("注册");
        //把监听器注册到控件上
        registerBtn.addActionListener(e -> {
            framesManager.switchToReg(); // 创建注册窗体
        });
        buttonPanel1.add(registerBtn);

//        buttonPanel1.setLayout(new FlowLayout());

        getContentPane().add(buttonPanel1, "South");


        //设置窗体可见
        setVisible(true);
    }


}
