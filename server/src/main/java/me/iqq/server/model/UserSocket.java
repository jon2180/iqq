package me.iqq.server.model;

import me.iqq.common.protocol.DataWrapper;
import me.iqq.common.protocol.Friend;
import me.iqq.common.protocol.type.CommandCode;
import me.iqq.common.protocol.type.DataType;
import me.iqq.common.utils.Serial;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Vector;

public class UserSocket {
    /**
     * 用户id
     */
    private String userID;

    /**
     * 与用户相绑定的通道
     */
    private SocketChannel channel;

    public UserSocket(SocketChannel initialChannel) {
        this(null, initialChannel);
    }

    public UserSocket(String initialUserID, SocketChannel initialChannel) {
        userID = initialUserID;
        channel = initialChannel;
    }

    /**
     * @return the userId
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 通知所有好友，xxx上线了
     */
    public void notifyFriend(UserSocket userSocket) throws IOException {
        if (userID != null && userSocket.isOpen()) {
            DataWrapper data = new DataWrapper(CommandCode.NOTIFY_ONLINE, DataType.PureString, userID + "上线了");
//            data.setType(DataType.PureString);
            data.setStatusCode(200);
            userSocket.send(data);
        }
    }

    public void notifyFriends(Vector<Friend> friends, SocketManager socketManager) {
        for (Friend friend : friends) {
            UserSocket userSocket = socketManager.find(friend.getTargetUserId());
            if (userSocket != null) {
                try {
                    userSocket.notifyFriend(userSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isOpen() {
        return channel != null && channel.isOpen();
    }

    public void send(DataWrapper data) throws IOException {
        if (data != null)
            channel.write(Serial.objectToByteBuffer(data));
    }

    public void setUserID(String qq) {
        userID = qq;
    }
}
