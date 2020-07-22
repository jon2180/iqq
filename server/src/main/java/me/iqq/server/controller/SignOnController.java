package me.iqq.server.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import me.iqq.common.api.LogonApiProto;
import me.iqq.common.protocol.MessagePacket;

import java.nio.channels.SocketChannel;

public class SignOnController extends BaseController {

    @Override
    public void handle(MessagePacket packet, SocketChannel channel) {
        // ResultSet resultSet;
        LogonApiProto.LogonReq req = null;
        try {
            req = LogonApiProto.LogonReq.parseFrom(packet.getBody());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        if (req == null)
            return;

        String nickname = req.getUsername();
        String password = req.getPassword();


//        String
//        try {
//
//            // 随机出来一个QQ号
//            String qq;
//
//            boolean isRepeat;
//
//            do {
//                qq = Utils.getRandom();
//
//                sql = "select qnumber, nickname from accounts where qnumber='" + qq + "'";
//
//                // 判断QQ号是否已经被占用
//                isRepeat = dbConnection.query(sql).next();
//            } while (isRepeat);
//
//            account.setQQ(qq);
//            DBUtils.createUser(account);
//
//            // 把账户返回给客户端
//            account.setQQ(qq);
//            DataWrapper data = new DataWrapper(CommandCode.REG, DataType.UserObject, account);
//            data.setStatusCode(200);
//
//            System.out.println("Successful: " + qq + ":" + nickname);
//
//            response.send(data);
//
//        } catch (IOException | SQLException ioE) {
//            ioE.printStackTrace();
//        }
    }
}
