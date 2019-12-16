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

    Vector<Friends> friends = null;

    static {
        chatMap = new HashMap<String, ChatFrame>();
    }

    FriendsFrame() {
        this(new Vector<Friends>());
    }

    public FriendsFrame(Vector<Friends> friends) {
        friendsFrame = new JFrame("好友列表");
//        this.fc = friendsFrame.getContentPane();
//        friendsFrame.setLayout(null);
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
            String friendQQ = friend.getTarget_account();
            JButton friendsBtn = new JButton(friendQQ);
            friendsBtn.setPreferredSize(new Dimension(200, 50));
            friendsBtn.setLocation(200 * i, 50 * i);
            friendsBtn.addActionListener(e -> {
                // 如果还没有打开聊天页面，则打开聊天页面
                if (!FriendsFrame.chatMap.containsKey(friendQQ)) {
                    ChatFrame chatFrame = new ChatFrame(friendQQ);
                    FriendsFrame.chatMap.put(friendQQ, chatFrame);
//                    Messages message = new Messages(friendQQ, "1234567", "发送第一条消息", 1);
//                message.setId(1);
//                message.setOrigin_account(friendQQ);
//                message.setTarget_account("1234567");
//                message.setContent("发送第一条消息");
//                message.setType(1);
//                    FriendsFrame.displayMessage(message);
                }
                // 否则用于自己去找对应的聊天窗口
//                FriendsFrame.chatMap.get(friendQQ).getChatFrame();

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

    public static void displayMessage(Messages message) {
        String friendQQ = message.getTarget_account();
        if (FriendsFrame.chatMap.containsKey(friendQQ)) {
            FriendsFrame.chatMap.get(friendQQ).receiveMessage(message, 1576482028);
        }
//        if (!FriendsFrame.chatMap.containsKey(friendQQ)) {
//            ChatFrame chatFrame = new ChatFrame(friendQQ);
//            chatFrame.receiveMessage(message, 1576482028);
//            FriendsFrame.chatMap.put(friendQQ, chatFrame);
//        } else {
//            FriendsFrame.chatMap.get(friendQQ).receiveMessage(message, 1576482028);
//        }
    }

    public static void main(String[] args) {
        new FriendsFrame();
    }
}
