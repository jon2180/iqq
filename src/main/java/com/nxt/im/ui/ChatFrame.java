package com.nxt.im.ui;

import com.nxt.im.common.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import java.awt.FlowLayout;
// import java.awt.GridLayout;

public class ChatFrame extends JFrame {

    private JFrame chatFrame;
    private Container cc;
    private JButton sendbtn;
    private JTextArea jta;
    private JTextField jtf;
    private JTextPane msgArea;
    private JScrollPane textScrollPane;
    private JScrollBar vertical;
    private JTextArea jtashow;

    private JList<String> userlist;
    DefaultListModel<String> users_model;

    public ChatFrame() {
        this.chatFrame = new JFrame("chat room");
        this.cc = chatFrame.getContentPane();
        this.sendbtn = new JButton("发送");
        this.jta = new JTextArea(15, 35);
        this.jtf = new JTextField(15);
        this.msgArea = new JTextPane();
        this.textScrollPane = new JScrollPane();
        this.vertical = new JScrollBar(JScrollBar.VERTICAL);
        this.jtashow = new JTextArea(20, 35);
        //this.friendsbtn = new JButton();

        //设置窗体的位置及大小
        chatFrame.setBounds(500, 100, 600, 500);
        //设置一层相当于桌布的东西
        cc.setLayout(new BorderLayout());//布局管理器
        //设置按下右上角X号后关闭
        chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置窗体可见
        chatFrame.setVisible(true);
        //初始化--往窗体里放其他控件

        init();
    }

    public void init() {
        /*标题部分--North*/
        JPanel titlePanel1 = new JPanel();
        titlePanel1.setLayout(new FlowLayout());
        titlePanel1.add(new JLabel("fakeque"));
        cc.add(titlePanel1, "North");

        /*输入部分--Center*/
        JPanel fieldPanel = new JPanel();
        //fieldPanel.setLayout(null);
        fieldPanel.add(jtf);
        // msgArea.setEditable(false);
        // textScrollPane.setViewportView(msgArea);
        // vertical.setAutoscrolls(true);
        // textScrollPane.setVerticalScrollBar(vertical);
        // fieldPanel.add(msgArea);
        cc.add(fieldPanel);

        JPanel textPanel = new JPanel();
        jtashow.setEditable(false);
        JScrollPane jspshow = new JScrollPane(jtashow);
        jspshow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jspshow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textPanel.add(jspshow);
        textPanel.add(jta);
        cc.add(textPanel);

        // users_model = new DefaultListModel<>();
        // userlist = new JList<>(users_model);
        // JScrollPane userListPane = new JScrollPane(userlist);

        // JScrollPane userPane = new JScrollPane();
        // userPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        // for(int i=0;i<10;i++)
        // {
        // 	String friend_name = nameStd[i];
        //     friendsbtn = new JButton(friend_name);
        //     friendsbtn.setPreferredSize(new Dimension(200,50));
        //     friendsbtn.setLocation(200*i, 50*i);
        //     userPane = new JScrollPane(friendsbtn);
        //     JPanel userPanel = new JPanel();
        //     userPanel.add(userPane,"West");
        //     cc.add(userPanel);
        // }
        // userPanel.add(userListPane, "West");
        // cc.add(userPanel,"West");
        // JPanel textPanel = new JPanel();
        // textPanel.add(jta,"Center");
        /*按钮部分--South*/
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(sendbtn);
        cc.add(buttonPanel, "South");

        //创建 send 监听器对象
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

    public void receiveMessage(Messages message, long time) {
//        System.out.println(content);
        jtashow.append(message.getOrigin_account() + "  " + time + ":\r\n" + message.getContent() + "\r\n");
        jtashow.setLineWrap(true);
        jta.setText("");
    }
}
