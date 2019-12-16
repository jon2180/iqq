package com.nxt.im.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.util.Iterator;
import java.util.Vector;

import com.nxt.im.common.Accounts;
import com.nxt.im.common.DataByteBuffer;
import com.nxt.im.common.Friends;
import com.nxt.im.common.Messages;
import com.nxt.im.db.DatabaseConnection;

/**
 * 统一分发处理路由
 *
 * @version 191215
 * @description 在考虑具体实现方式
 */
public class Router {

    private static DatabaseConnection dbConnection;

    static {
        dbConnection = new DatabaseConnection();
    }

    private Router() {

    }

    public static void dispatch(SocketChannel socketChannel, ByteBuffer byteBuffer) {

        DataByteBuffer dataByteBuffer;

        try {
            dataByteBuffer = new DataByteBuffer(byteBuffer);
            String url = dataByteBuffer.getUrl();
            switch (url) {
                case "/user/reg":
                    registerUser(socketChannel, (Accounts) dataByteBuffer.getData());
                    break;
                case "/user/login":
                    loginCheck(socketChannel, (Accounts) dataByteBuffer.getData());
                    break;
            }
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("in Router.dispatch");
            e.printStackTrace();
        }
    }

    /**
     * 注册用户
     *
     * @param socketChannel 用来给客户端回复消息
     * @param account       需要的数据
     */
    private static void registerUser(SocketChannel socketChannel, Accounts account) {
        // ResultSet resultSet;
        String nickname = account.getNickname();
        String password = account.getPassword();
        String sql;
        try {

            // 随机出来一个QQ号
            String qq;

            boolean isRepeat;

            do {
                qq = Message.getRandom();

                sql = "select qnumber, nickname from accounts where qnumber='" + qq + "'";

                // 判断QQ号是否已经被占用
                isRepeat = dbConnection.query(sql).next();
            } while (isRepeat);

            sql = "insert into accounts (qnumber, nickname, password) values('" + qq + "','" + nickname + "', '" + password
                + "')";
            dbConnection.update(sql);

            // 把账户返回给客户端
            account.setQnumber(qq);
            DataByteBuffer data = new DataByteBuffer("/user/reg", account);
            data.setStatusCode(200);
            System.out.println(data.getStatusCode());

            System.out.println("注册成功：" + qq + ":" + nickname);

            DataByteBuffer tmp = new DataByteBuffer(data.toByteBuffer());
            System.out.println(tmp.getStatusCode());

            socketChannel.write(data.toByteBuffer());

        } catch (IOException | SQLException | ClassNotFoundException ioE) {
            ioE.printStackTrace();
        }
    }

    /**
     * 触发登录验证
     */
    private static void loginCheck(SocketChannel socketChannel, Accounts account) {
        ResultSet resultSet;
        String qnumber = account.getQnumber();
        String password = account.getPassword();
        String sql;
        try {
            sql = "SELECT id,qnumber,nickname,password FROM accounts where qnumber = '" + qnumber + "'";
            resultSet = dbConnection.query(sql);

            // x 账户不存在
            if (resultSet.next()) {
                System.out.println("无此用户");
                String msg = "10002";
                socketChannel.write(Message.encode(msg));
                return;
            }

            String realPassword = resultSet.getString("password");

            System.out.println("password:" + password);
            System.out.println("realPassword:" + realPassword);

            // x 登录账户与密码不匹配
            if (realPassword.equals(password)) {
                System.out.println("登录失败，密码错误");
                return;
            }

            // v 正常登录
            System.out.println("成功登录");

            // 把当前用户的 qq号 作为键，对应的 SocketWrapper 对象作为值，放进 socketMap 中
            NioServer.getSocketMap().put(qnumber, new SocketWrapper(qnumber, socketChannel));

            // TODO 获取到用户的好友列表
            Vector<Friends> friends = SocketWrapper.getAllFridents(qnumber);

            // TODO 获取各好友的在线状态，并通知在线好友：“我上线了”
            if (friends != null) {
                SocketWrapper.notifyAllFriends(qnumber, friends);
            }

            // TODO 发送消息给客户端：好友列表
            socketChannel.write(new DataByteBuffer("/user/friends", friends).toByteBuffer());
        } catch (IOException | SQLException ioE) {
            ioE.printStackTrace();
        }
    }

    /**
     * 发送消息给指定客户端
     *
     * @param dataBuf 消息对象
     */
    public static void sendMessage(DataByteBuffer dataBuf) {

        Messages message = (Messages) dataBuf.getData();

        String friendQQ = message.getTarget_account();
        String content = message.getContent();

        if (NioServer.getSocketMap().containsKey(friendQQ)) {
            try {
                // TODO 发送消息
                NioServer.getSocketMap().get(friendQQ).getChannel().write(Message.encode(content));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
