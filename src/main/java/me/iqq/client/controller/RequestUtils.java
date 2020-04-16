package me.iqq.client.controller;

import me.iqq.client.model.FramesManager;
import me.iqq.client.GlobalData;
import me.iqq.client.model.SocketWrapper;
import me.iqq.common.protocol.DataWrapper;
import me.iqq.common.protocol.Message;
import me.iqq.common.protocol.User;
import me.iqq.common.protocol.type.CommandCode;
import me.iqq.common.protocol.type.DataType;
import me.iqq.common.utils.Serial;

import java.io.IOException;

public class RequestUtils {

    private static SocketWrapper socket;

    private static FramesManager framesManager;

    private RequestUtils() {
        // avoid creating new instance
    }

    public static void setSocketWrapper(SocketWrapper newSocket) {
        socket = newSocket;
    }

    public static void setFramesManager(FramesManager fm) {
        framesManager = fm;
    }

    /**
     * 注册
     *
     * @param nickname 昵称
     * @param password 密码
     */
    public static void register(String nickname, String password) {
        User account = new User();
        account.setNickname(nickname);
        account.setPassword(password);
        DataWrapper dataWrapper = new DataWrapper(CommandCode.REG, DataType.UserObject, account);
        try {
            socket.send(Serial.objectToByteBuffer(dataWrapper));
//            framesManager.switchToLogin();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 登录
     *
     * @param qq       昵称
     * @param password 密码
     */
    public static void login(String qq, String password) {
        User account = new User(qq, password);
        DataWrapper dataWrapper = new DataWrapper(CommandCode.LOG_IN, DataType.UserObject, account);
        try {
            socket.send(Serial.objectToByteBuffer(dataWrapper));
            framesManager.setId(qq);
            GlobalData.setMyAccount(qq);
            System.out.println("Sent login request successfully");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Sent login request failed");
        }
    }


    /**
     * 发送消息
     *
     * @param message 消息对象
     * @throws IOException 发送失败
     */
    public static void sendMessage(Message message) throws IOException {
        if (message != null) {
            DataWrapper data = new DataWrapper(CommandCode.SEND_MESSAGE, DataType.MessageObject, message);
            socket.send(Serial.objectToByteBuffer(data));
        }
    }
}
