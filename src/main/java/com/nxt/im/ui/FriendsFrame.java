package com.nxt.im.ui;

import com.nxt.im.common.Friends;
import com.nxt.im.common.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

// import java.awt.FlowLayout;
// import java.awt.GridLayout;

/**
 * FriendsFrame
 *
 * @version 191216
 */
public class FriendsFrame {
    //    extends JFrame
    private JFrame friendsFrame;
    //    private Container fc;
//    private JScrollPane jsp;
    private static HashMap<String, ChatFrame> chatMap;

    private String myQQ;

    Vector<Friends> friends = null;

    static {
        chatMap = new HashMap<String, ChatFrame>();
    }

    FriendsFrame() {
        this(new Vector<Friends>());
    }

    public FriendsFrame(Vector<Friends> friends) {
        friendsFrame = new JFrame("好友列表");
        friendsFrame.setLayout(new FlowLayout());
        friendsFrame.setTitle("好友列表");
        friendsFrame.setSize(260, 600);
        friendsFrame.setLocation(900, 50);
        friendsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.friends = friends;
        updateFriendsList();

        friendsFrame.setVisible(true);
    }

    public static HashMap<String, ChatFrame> getChatMap() {
        return chatMap;
    }

    public void updateFriendsList() {
        if (friends == null)
            return;
        JPanel userPanel = new JPanel();
        int i = 0;
        for (Friends friend : friends) {
            myQQ = friend.getOrigin_account();
            String friendQQ = friend.getTarget_account();
            JButton friendsBtn = new JButton(friendQQ);
            friendsBtn.setPreferredSize(new Dimension(200, 50));
            friendsBtn.setLocation(200 * i, 50 * i);
            friendsBtn.addActionListener(e -> {
                // 如果还没有打开聊天页面，则打开聊天页面
                if (!FriendsFrame.chatMap.containsKey(friendQQ)) {
                    ChatFrame chatFrame = new ChatFrame(myQQ, friendQQ);
                    FriendsFrame.chatMap.put(friendQQ, chatFrame);
                }

            });
            userPanel.add(friendsBtn);
            i++;
        }
        JScrollPane userPane = new JScrollPane(userPanel);
        userPane.setSize(260, 600);
        userPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        userPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        friendsFrame.getContentPane().add(userPane, "East");
    }

    /**
     * 接收来自其他用户的消息，注意：消息的 target_account 是自己，origin_account 是消息发送方
     * @param myQQ 客户qq号
     * @param message 消息对象
     * @param time 时间戳（精确到毫秒）
     */
    public static void displayMessage(String myQQ, Messages message, long time) {
        String friendQQ = message.getOrigin_account();
        if (!FriendsFrame.chatMap.containsKey(friendQQ)) {
            ChatFrame chatFrame = new ChatFrame(myQQ, friendQQ);
            chatFrame.receiveMessage(message, time);
            FriendsFrame.chatMap.put(friendQQ, chatFrame);
        } else {
            FriendsFrame.chatMap.get(friendQQ).receiveMessage(message, time);
        }
    }

    public static void main(String[] args) {
        new FriendsFrame();
    }
}
