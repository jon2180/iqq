package me.iqq.client.ui;

import me.iqq.common.utils.FormatTools;

import javax.swing.*;
import java.awt.*;

/**
 * ChatFrame
 *
 * @version 191216
 */
public class ChatFrame extends JFrame {
    private JTextArea jta;
    private JTextArea jtashow;

    public ChatFrame(String myQQ, String targetQQ) {
        super("IQQ");
        Container cc = getContentPane();
        JButton sendBtn = new JButton("发送");
        this.jta = new JTextArea(15, 35);
        JTextField jtf = new JTextField(15);
        JTextPane msgArea = new JTextPane();
        JScrollPane textScrollPane = new JScrollPane();
        JScrollBar vertical = new JScrollBar(JScrollBar.VERTICAL);
        this.jtashow = new JTextArea(20, 35);

        //设置窗体的位置及大小
        setBounds(500, 100, 600, 500);
        //设置一层相当于桌布的东西
        cc.setLayout(new BorderLayout());//布局管理器
        //设置按下右上角X号后关闭
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 设置窗体可见
        setVisible(true);
        //初始化--往窗体里放其他控件

        // 标题部分 -- North
        JPanel titlePanel1 = new JPanel();
        titlePanel1.setLayout(new FlowLayout());
        titlePanel1.add(new JLabel("Chatting with " + targetQQ));
        cc.add(titlePanel1, "North");

        // 输入部分 -- Center
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
        buttonPanel.add(sendBtn);
        cc.add(buttonPanel, "South");

        //创建 send 监听器对象
        sendBtn.addActionListener(e -> {
            String content = jta.getText();
            if (content.length() < 1) {
                return;
            }
            long time = System.currentTimeMillis();
            jtashow.append(myQQ + "  " + FormatTools.formatTimestamp(time) + "\r\n" + content + "\r\n\n");
            jtashow.setLineWrap(true);
            jta.setText("");

            // TODO 发送消息
//            try {
//                RequestUtils.sendMessage(new Message(myQQ, targetQQ, content, 0));
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
        });
    }

    /**
     * 显示消息到聊天窗口
     * 注意：这里的 message.getOrigin_account() 是消息发送方
     *
     * @param message Messages对象
     * @param time    时间戳
     */
//    public void receiveMessage(Message message, long time) {
//        System.out.println(message.getOriginAccount() + ": " + message.getContent());
//        jtashow.append(message.getOriginAccount() + "  " + FormatTools.formatTimestamp(time) + ":\r\n" + message.getContent() + "\r\n");
//        jtashow.setLineWrap(true);
//        jta.setText("");
//    }
}
