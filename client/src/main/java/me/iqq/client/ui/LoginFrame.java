package me.iqq.client.ui;

import me.iqq.client.ui.model.FramesManager;
import me.iqq.client.request.RequestFactory;
import me.iqq.client.request.UserRequest;
import me.iqq.client.ui.actions.JTextFieldPlaceholderListener;
import me.iqq.client.ui.base.Singleton;
import me.iqq.common.utils.Validator;

import javax.swing.*;
import java.awt.*;

/**
 * 登录面板 JFrame
 */
public class LoginFrame extends JFrame implements Singleton {

    private static final long serialVersionUID = 1L;

    private final FramesManager framesManager;

    public LoginFrame(FramesManager fm) {
        super("QQ");
        framesManager = fm;

        //设置窗体的位置及大小
        setBounds(600, 200, 300, 220);

        //设置一层相当于桌布的东西
        setLayout(new BorderLayout());

        //设置按下右上角X号后关闭
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 初始化--往窗体里放其他控件

        // 标题部分--North
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("登录"));
        add(titlePanel, BorderLayout.NORTH);

        // 输入部分 -- Center
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new FlowLayout());
        fieldPanel.setSize(300, 120);

        // QQ 号
//        JLabel qqLabel = new JLabel("QQ号");
//        qqLabel.setBounds(50, 20, 50, 20);
//        fieldPanel.add(qqLabel);

        JTextField qqTextField = new JTextField("", 20);
        qqTextField.addFocusListener(new JTextFieldPlaceholderListener(qqTextField, "QQ"));
//        qqTextField.setBounds(110, 20, 120, 20);
        fieldPanel.add(qqTextField);

        // 密码
//        JLabel a2 = new JLabel("密   码");
//        a2.setBounds(50, 60, 50, 20);
//        fieldPanel.add(a2);

        JPasswordField password = new JPasswordField("", 20);
        password.addFocusListener(new JTextFieldPlaceholderListener(password, "密码"));
//        password.setBounds(110, 60, 120, 20);
        fieldPanel.add(password);

        // 提示
        JLabel noticeLabel = new JLabel("");
//        noticeLabel.setBounds(50, 100, 170, 20);
        noticeLabel.setText("");
        fieldPanel.add(noticeLabel);

        // 添加到 frame
        add(fieldPanel, BorderLayout.CENTER);

        // 按钮部分--South
        JPanel buttonPanel = new JPanel();

        JButton certainBtn = new JButton("确定");
        //把监听器注册到控件上
        certainBtn.addActionListener(e -> {
            String id = qqTextField.getText();
            String pwd = String.valueOf(password.getPassword());

            if (!Validator.checkPassword(pwd)) {
                noticeLabel.setText("你的账号/密码格式不正确");
                return;
            }

            final String message = "wrong username or password!";
            final String title = "ERROR";

            if (id.equals("") || pwd.equals("")) {
                System.out.println("QQ号或密码不能为空");
                noticeLabel.setText("账号、密码错误");

//                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
            } else {
                // TODO
                //  lazy load channel
                final UserRequest ur = RequestFactory.getUserRequest();
                ur.login(id, pwd);
            }

        });
        buttonPanel.add(certainBtn);

        // 取消按钮
        JButton cancelBtn = new JButton("重置");
        //把监听器注册到控件上
        cancelBtn.addActionListener(e -> {
            password.setText("");
            qqTextField.setText("");
        });
        buttonPanel.add(cancelBtn);

        // 注册按钮
        JButton registerBtn = new JButton("注册");
        //把监听器注册到控件上
        registerBtn.addActionListener(e -> {
            framesManager.switchToReg(); // 创建注册窗体
        });
        buttonPanel.add(registerBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        //设置窗体可见
        setVisible(true);
    }

}
