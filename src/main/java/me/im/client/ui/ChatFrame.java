package me.im.client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.SimpleDateFormat;

// import java.awt.FlowLayout;
// import java.awt.GridLayout;
import me.im.client.socket.ClientRouter;
import me.im.common.protocol.Messages;

/**
 * ChatFrame
 *
 * @version 191216
 */
public class ChatFrame {
    // extends JFrame
    private JFrame chatFrame;
    private Container cc;
    private JButton sendbtn;
    private JTextArea jta;
    private JTextField jtf;
    private JTextPane msgArea;
    private JScrollPane textScrollPane;
    private JScrollBar vertical;
    private JTextArea jtashow;

    private String friendQQ;
    private String myQQ;

//    private JList<String> userlist;
//    DefaultListModel<String> users_model;

    public ChatFrame(String myQQ, String targetQQ) {
        this.myQQ = myQQ;
        this.friendQQ = targetQQ;
        this.chatFrame = new JFrame("fake-QQ");
        this.cc = chatFrame.getContentPane();
        this.sendbtn = new JButton("发送");
        this.jta = new JTextArea(15, 35);
        this.jtf = new JTextField(15);
        this.msgArea = new JTextPane();
        this.textScrollPane = new JScrollPane();
        this.vertical = new JScrollBar(JScrollBar.VERTICAL);
        this.jtashow = new JTextArea(20, 35);

        //设置窗体的位置及大小
        chatFrame.setBounds(500, 100, 600, 500);
        //设置一层相当于桌布的东西
        cc.setLayout(new BorderLayout());//布局管理器
        //设置按下右上角X号后关闭
        chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chatFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {
                closeWindow();
            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });
        // 设置窗体可见
        chatFrame.setVisible(true);
        //初始化--往窗体里放其他控件

        init();
    }

    public void setChatFrameTitle(String title) {
        this.chatFrame.setTitle(title);
    }

    public JFrame getChatFrame() {
        return chatFrame;
    }

    public void init() {
        /*标题部分--North*/
        JPanel titlePanel1 = new JPanel();
        titlePanel1.setLayout(new FlowLayout());
        titlePanel1.add(new JLabel("Chatting with " + friendQQ));
        cc.add(titlePanel1, "North");

        /*输入部分--Center*/
        JPanel fieldPanel = new JPanel();
        fieldPanel.add(jtf);
        cc.add(fieldPanel);

        JPanel textPanel = new JPanel();
        jtashow.setEditable(false);
        JScrollPane jspshow = new JScrollPane(jtashow);
        jspshow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jspshow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textPanel.add(jspshow);
        textPanel.add(jta);
        cc.add(textPanel);

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
                if (content.length() < 1) {
                    return;
                }
                long time = System.currentTimeMillis();
//                System.out.println(content);
                jtashow.append(myQQ + "  " + formatTimestamp(time) + "\r\n" + content + "\r\n");
                jtashow.setLineWrap(true);
                jta.setText("");

                try {
                    ClientRouter.getClient().send(new Messages(myQQ, friendQQ, content, 0));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        sendbtn.addActionListener(ca);
    }

    public void closeWindow() {
        FriendsFrame.getChatMap().remove(friendQQ);
        chatFrame.dispose();
    }

    /**
     * 格式化时间戳
     *
     * @param timestamp 时间戳 十三位
     * @return 格式化后的时间戳
     */
    public String formatTimestamp(long timestamp) {
        return new SimpleDateFormat("MM/dd HH:mm").format(timestamp);
    }

    /**
     * 显示消息到聊天窗口
     * 注意：这里的 message.getOrigin_account() 是消息发送方
     *
     * @param message Messages对象
     * @param time    时间戳
     */
    public void receiveMessage(Messages message, long time) {
        jtashow.append(message.getOrigin_account() + "  " + formatTimestamp(time) + ":\r\n" + message.getContent() + "\r\n");
        jtashow.setLineWrap(true);
        jta.setText("");
    }
}
