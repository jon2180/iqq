package me.im.client.ui;

import me.im.client.socket.ClientRouter;
import me.im.common.protocol.Accounts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * loginFrame
 *
 * @description 注意：此类不能直接使用 new 关键字实例化，需要使用 getInstance 方法
 */
public class RegisterFrame {
//     extends JFrame

    /**
     * 提供单例模式实现，一个程序只有一个此界面
     */
    private static volatile RegisterFrame instance = null;

    /**
     * 版本号
     */
    private static final long serialVersionUID = 1L;

    private JFrame registerFrame;
    private Container rc;
    private JLabel a1;
    private JTextField username;
    private JLabel a2;
    private JPasswordField password;
    private JPasswordField rePassword;
    private JLabel a3;
    private JButton certainBtn;
    private JButton cancelBtn;

    private Accounts accounts;
    private int statusCode;

    public static RegisterFrame getInstance() {
        if (instance == null) {
            synchronized (RegisterFrame.class) {
                // if (instance == null) {
                instance = new RegisterFrame();
                // }
            }
        }
        return instance;
    }

    private RegisterFrame() {
        this.registerFrame = new JFrame("注册");
        this.rc = registerFrame.getContentPane();
        this.a1 = new JLabel("用户名");
        this.username = new JTextField();
        this.a2 = new JLabel("密   码");
        this.password = new JPasswordField();
        this.rePassword = new JPasswordField();
        this.a3 = new JLabel("确认密码");
        this.certainBtn = new JButton("确定");
        this.cancelBtn = new JButton("取消");

        // 设置窗体的位置及大小
        registerFrame.setBounds(700, 300, 400, 300);
        // 设置一层相当于桌布的东西
        rc.setLayout(new BorderLayout());// 布局管理器
        // 设置按下右上角X号后关闭
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗体可见
        registerFrame.setVisible(true);
        // 初始化--往窗体里放其他控件
        init();

        System.out.println("已执行完毕init");
    }

    public void init() {
        /* 标题部分--North */
        JPanel titlePanel1 = new JPanel();
        titlePanel1.setLayout(new FlowLayout());
        titlePanel1.add(new JLabel("fake-qq"));
        rc.add(titlePanel1, "North");

        /* 输入部分--Center */
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
        rePassword.setBounds(110, 100, 120, 20);
        fieldPanel1.add(username);
        fieldPanel1.add(password);
        fieldPanel1.add(rePassword);
        rc.add(fieldPanel1, "Center");

        /* 按钮部分--South */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(certainBtn);
        buttonPanel.add(cancelBtn);
        rc.add(buttonPanel, "South");

        // 创建 确定按钮 监听器对象
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //

                String nickname = String.valueOf(username.getText());
                String pwd = String.valueOf(password.getPassword());
                String repwd = String.valueOf(rePassword.getPassword());


                Object message = "you connot input a null value!";
                String title = "ERROR";
                if ((nickname == null || nickname.length() <= 0) || (pwd == null || pwd.length() <= 0) || (repwd == null || repwd.length() <= 0)) {
                    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
                } else {
                    // System.out.println(nickname + "\r\n" + pwd + "\r\n" + repwd);
                    // registerFrame.setVisible(false);

                    if (ClientRouter.userReg(nickname, pwd)) {
                        System.out.println("发送成功: 注册");
                    } else {
                        System.out.println("发送失败: 注册");
                    }
                }
            }
        };
        certainBtn.addActionListener(actionListener);

        // 创建 取消按钮 监听器对象
        ActionListener racan = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username.setText("");
                password.setText("");
                rePassword.setText("");
            }
        };
        // 把监听器注册到控件上
        cancelBtn.addActionListener(racan);

    }

    public void register(int code, Accounts acnt) {
        // 展示提示信息
        System.out.println(code);
        if (code == 200) {
            JOptionPane.showMessageDialog(new JPanel(), accounts.getQnumber(), "您的QQ账号，请牢记", JOptionPane.INFORMATION_MESSAGE);

            registerFrame.setVisible(false);
            // 创建登录窗体
            LoginFrame ft = LoginFrame.getInstance();

        } else {
            JOptionPane.showMessageDialog(new JPanel(), "注册过程出错", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        RegisterFrame.getInstance();
    }
}