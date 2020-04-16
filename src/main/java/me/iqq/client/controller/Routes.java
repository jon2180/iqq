package me.iqq.client.controller;

import me.iqq.client.model.FramesManager;
import me.iqq.client.GlobalData;
import me.iqq.client.ui.ChatFrame;
import me.iqq.common.protocol.DataWrapper;
import me.iqq.common.protocol.Friend;
import me.iqq.common.protocol.Message;
import me.iqq.common.protocol.User;
import me.iqq.common.protocol.type.DataType;

import javax.swing.*;
import java.nio.channels.SocketChannel;
import java.util.Vector;

public class Routes {

    private FramesManager framesManager;

    public Routes(FramesManager fm) {
        framesManager = fm;
    }

    public void register(DataWrapper request, SocketChannel response) {

        int statusCode = request.getStatusCode();
        long time = request.getTime();
        DataType type = request.getType();
        User user = request.getUser();
        // 展示提示信息
        if (statusCode == 200) {
            JOptionPane.showMessageDialog(new JPanel(), user.getQQ(), "您的QQ账号，请牢记", JOptionPane.INFORMATION_MESSAGE);
            framesManager.switchToLogin();
        } else {
            JOptionPane.showMessageDialog(new JPanel(), "注册过程出错", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void login(DataWrapper request, SocketChannel response) {

        int statusCode = request.getStatusCode();
        long time = request.getTime();
        DataType type = request.getType();

        if (statusCode != 200 && type == DataType.PureString) {
            JOptionPane.showMessageDialog(new JPanel(), request.getString(), "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (statusCode != 200 || type == DataType.FriendsVector) {
            JOptionPane.showMessageDialog(new JPanel(), null, "错误", JOptionPane.WARNING_MESSAGE);
        }

        framesManager.switchToMain();
        Vector<Friend> f = request.getFriends();
        GlobalData.setFriends(f);
        framesManager.getFrame().render(request.getFriends());
    }

    public void sendMessage(DataWrapper request, SocketChannel response) {
        int statusCode = request.getStatusCode();
        long time = request.getTime();
        DataType type = request.getType();

        Message message = request.getMessage();
        ChatFrame chatFrame = framesManager.createChatFrame(message.getOriginAccount());
        chatFrame.receiveMessage(message, time);
    }

    public void notifyFriends(DataWrapper request, SocketChannel response) {
        System.out.println(request.getString());
    }

    public void notFound(DataWrapper request, SocketChannel response) {
        System.out.println("暂无此url选项");
    }

}
