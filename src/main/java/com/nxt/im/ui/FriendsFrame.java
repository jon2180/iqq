package com.nxt.im.ui;

import com.nxt.im.common.Friends;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

// import java.awt.FlowLayout;
// import java.awt.GridLayout;
public class FriendsFrame extends JFrame {
    private JFrame friendsFrame;
    private JButton friendsbtn;
    private Container fc;
    private JScrollPane jsp;


    Vector<Friends> friends = null;

    String[] nameStd = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};

    FriendsFrame() {
        this.friendsFrame = new JFrame("好友列表");
        this.fc = friendsFrame.getContentPane();
        friendsFrame.setLayout(new FlowLayout());
        //friendsFrame.setTitle("friendsframe");
        friendsFrame.setSize(260, 600);
        friendsFrame.setLocation(900, 50);
        friendsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        friendsFrame.setVisible(true);
        init();
    }

    public FriendsFrame(Vector<Friends> friends) {
        this();
        this.friends = friends;

        //                    System.out.println((i++) + ": " + ((Friends) friend).getTarget_account());

        //            Scanner scanner = new Scanner(System.in);
//            int index = scanner.nextInt();
//            if (index > 0 && index <= i) {
//                Chat c = new Chat();
////                ClientRouter.getSocketChannel().write()
//            }
    }


    public void init() {

        //jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JPanel userPanel = new JPanel();
        for (int i = 0; i < 10; i++) {
            String friend_name = nameStd[i];
            friendsbtn = new JButton(friend_name);
            friendsbtn.setPreferredSize(new Dimension(200, 50));
            // friendsbtn.setLocation(200*i, 50*i);
            //userPane = new JScrollPane(friendsbtn);

            userPanel.add(friendsbtn);

            ActionListener ula = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //friendsFrame.setVisible(false);
                    new ChatFrame();
                    //jtashow.setText("");
                }
            };
            friendsbtn.addActionListener(ula);
        }
        JScrollPane userPane = new JScrollPane(userPanel);
        userPane.setSize(400, 100);
        userPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        userPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        fc.add(userPane, "East");
    }

    public static void main(String[] args) {
        new FriendsFrame();
    }
}
