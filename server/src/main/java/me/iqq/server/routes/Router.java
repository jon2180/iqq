package me.iqq.server.routes;

import me.iqq.common.protocol.DataWrapper;
import me.iqq.common.protocol.Friend;
import me.iqq.common.protocol.Message;
import me.iqq.common.protocol.User;
import me.iqq.common.protocol.type.CommandCode;
import me.iqq.common.protocol.type.DataType;
import me.iqq.common.utils.FormatTools;
import me.iqq.server.db.DBUtils;
import me.iqq.server.db.DatabaseConnection;
import me.iqq.server.model.SocketManager;
import me.iqq.server.model.UserSocket;
import me.iqq.server.utils.Utils;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * 统一分发处理路由
 *
 * @version 191215
 * 在考虑具体实现方式
 */
public class Router {

    private DatabaseConnection dbConnection;
    private SocketManager socketManager = new SocketManager();

    public Router() {
        dbConnection = new DatabaseConnection();
    }

    public void dispatch(DataWrapper request, SocketChannel socketChannel) {

        UserSocket response = new UserSocket(socketChannel);

        middleware(request, response);

        switch (request.getUrl()) {
            case REG:
                registerUser(request, response);
                break;
            case LOG_IN:
                loginCheck(request, response);
                break;
            case SEND_MESSAGE:
                sendMessage(request, response);
            default:
                notFound(request, response);
        }
    }

    private void notFound(DataWrapper dataWrapper, UserSocket response) {
    }

    public void middleware(DataWrapper dataWrapper, UserSocket socketChannel) {
        System.out.println(FormatTools.formatTimestamp(dataWrapper.getTime()) + " " + dataWrapper.getType() + " " + dataWrapper.getUrl());
    }

    /**
     * 注册用户
     *
     * @param request  需要的数据
     * @param response 用来给客户端回复消息
     */
    private void registerUser(DataWrapper request, UserSocket response) {
        // ResultSet resultSet;
        User account = request.getUser();
        String nickname = account.getNickname();
        String password = account.getPassword();
        String sql;
        try {

            // 随机出来一个QQ号
            String qq;

            boolean isRepeat;

            do {
                qq = Utils.getRandom();

                sql = "select qnumber, nickname from accounts where qnumber='" + qq + "'";

                // 判断QQ号是否已经被占用
                isRepeat = dbConnection.query(sql).next();
            } while (isRepeat);

            account.setQQ(qq);
            DBUtils.createUser(account);

            // 把账户返回给客户端
            account.setQQ(qq);
            DataWrapper data = new DataWrapper(CommandCode.REG, DataType.UserObject, account);
            data.setStatusCode(200);

            System.out.println("Successful: " + qq + ":" + nickname);

            response.send(data);

        } catch (IOException | SQLException ioE) {
            ioE.printStackTrace();
        }
    }

    /**
     * 触发登录验证
     */
    private void loginCheck(DataWrapper request, UserSocket response) {
        User account = request.getUser();
        ResultSet resultSet;
        String qq = account.getQQ();
        String password = account.getPassword();
        try {
            resultSet = DBUtils.find(qq);

            // x 账户不存在
            if (!resultSet.next()) {
                System.out.println("No this user");

                DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.PureString, "无此用户");
                data.setStatusCode(401);

                data.setType(DataType.PureString);
                response.send(data);
                return;
            }

            String realPassword = resultSet.getString("password");

            // x 登录账户与密码不匹配
            if (!realPassword.equals(password)) {
                System.out.println(qq + " login failed");
                DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.PureString, "登录密码错误");
                data.setStatusCode(401);
                response.send(data);
                return;
            }

            // v 正常登录
            System.out.println(qq + " login successfully");

            // 把当前用户的 qq号 作为键，对应的 SocketWrapper 对象作为值，放进 socketMap 中
            response.setUserID(qq);
            socketManager.put(qq, response);

            Vector<Friend> friends = DBUtils.getAllFriends(qq);

            // 获取各好友的在线状态，并通知在线好友：“我上线了”
            if (friends != null && !friends.isEmpty()) {
                response.notifyFriends(friends, socketManager);
            }

            // 发送消息给客户端：好友列表
            DataWrapper data = new DataWrapper(CommandCode.LOG_IN, DataType.FriendsVector, friends);
            response.send(data);
        } catch (IOException | SQLException ioE) {
            ioE.printStackTrace();
        }
    }

    /**
     * 发送消息给指定客户端
     *
     * @param dataBuf 消息对象
     */
    public void sendMessage(DataWrapper dataBuf, UserSocket response) {

        Message message = dataBuf.getMessage();

        String friendQQ = message.getTargetAccount();

        if (socketManager.containsKey(friendQQ)) {
            UserSocket userSocket = socketManager.get(friendQQ);
            try {
                if (!userSocket.isOpen()) {
                    socketManager.remove(friendQQ);
                } else {
                    userSocket.send(dataBuf);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
