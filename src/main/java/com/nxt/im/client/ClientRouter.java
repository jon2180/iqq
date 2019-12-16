package com.nxt.im.client;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import com.nxt.im.common.Accounts;
import com.nxt.im.common.DataByteBuffer;
import com.nxt.im.config.CommandCode;

public class ClientRouter {

    private static ClientSocket client;

    static {
        try {
            client = new ClientSocket("127.0.0.1", 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SocketChannel getSocketChannel() {
        return client.getSocketChannel();
    }

    public static ClientSocket getClient() {
        return client;
    }

    /**
     * 注册
     *
     * @param nickname 昵称
     * @param password 密码
     * @return 是否成功发送请求
     */
    public static boolean userReg(String nickname, String password) {
        Accounts account = new Accounts();
        account.setNickname(nickname);
        account.setPassword(password);
        DataByteBuffer dataByteBuffer = new DataByteBuffer(CommandCode.REG, account);
        try {
            ClientRouter.client.send(dataByteBuffer.toByteBuffer());
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    /**
     * 登录
     *
     * @param qnumber  昵称
     * @param password 密码
     * @return
     */
    public static boolean userLog(String qnumber, String password) {
        Accounts account = new Accounts();
        account.setQnumber(qnumber);
        account.setPassword(password);

        DataByteBuffer dataByteBuffer = new DataByteBuffer(CommandCode.LOG_IN, account);
        dataByteBuffer.setType("json");

        try {
            ClientRouter.client.send(dataByteBuffer.toByteBuffer());
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
}
