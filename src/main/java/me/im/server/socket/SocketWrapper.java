package me.im.server.socket;

import me.im.common.protocol.DataByteBuffer;
import me.im.common.protocol.Friends;
import me.im.common.utils.CommandCode;
import me.im.server.db.DatabaseConnection;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class SocketWrapper {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 与用户相绑定的通道
     */
    private SocketChannel channel;

    public SocketWrapper(String userId, SocketChannel channel) {
        this.userId = userId;
        this.channel = channel;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the channel
     */
    public SocketChannel getChannel() {
        return channel;
    }

    /**
     * 获取好友列表
     *
     * @return the fridents
     */
    public static Vector<Friends> getAllFridents(String qq) {
        Vector<Friends> friends = new Vector<Friends>();

        DatabaseConnection dbConn = new DatabaseConnection();

        String sql = "select id, target_account, group_name, type from friends where origin_account='" + qq + "'";

        try {
            ResultSet res = dbConn.query(sql);
            while (res.next()) {
                int friendId = res.getInt("id");
                String targetAccount = res.getString("target_account");
                String groupName = res.getString("group_name");
                int type = res.getInt("type");

                friends.add(new Friends(friendId, qq, targetAccount, groupName, type));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            dbConn.close();
        }

        // TODO 测试一下结果
        for (Friends friend : friends) {
            System.out.println(friend.getTarget_account());
        }

        return friends;
    }

    /**
     * 通知所有好友，xxx上线了
     *
     * @param qq      qq号
     * @param friends 好友列表
     */
    public static void notifyAllFriends(String qq, Vector<Friends> friends) {
        HashMap<String, SocketWrapper> map = NioServer.getSocketMap();
        Friends friend;
        Iterator<Friends> it = friends.iterator();
        SocketChannel channel = null;
        while (it.hasNext()) {
            friend = it.next();
            if (map.containsKey(friend.getTarget_account())) {
                try {
                    channel = map.get(friend.getTarget_account()).getChannel();
                    if (channel != null && channel.isOpen()) {
                        DataByteBuffer data = new DataByteBuffer(CommandCode.NOTIFY_ONLINE, qq + "上线了");
                        data.setType("String");
                        data.setStatusCode(200);
                        channel.write(data.toByteBuffer());
                    } else {
                        map.remove(qq);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
