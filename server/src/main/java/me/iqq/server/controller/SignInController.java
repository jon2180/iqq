package me.iqq.server.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import me.iqq.common.api.LoginApiProto;
import me.iqq.common.api.MessageHeaderProto;
import me.iqq.common.protocol.MessagePacket;
import me.iqq.common.protocol.MessageProtocol;
import me.iqq.server.service.UserService;
import me.iqq.server.service.impl.UserServiceImpl;
import me.iqq.server.utils.TokenUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class SignInController extends BaseController {

    private static final UserService userService = new UserServiceImpl();

    @Override
    public void handle(MessagePacket packet, SocketChannel channel) {
        LoginApiProto.LoginReq loginReq = null;
        try {
            loginReq = LoginApiProto.LoginReq.parseFrom(packet.getBody());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        if (loginReq == null) {
            log.warn("Parse bode failed");
            return;
        }

        // 1. 验证用户是否存在，存在则继续
        // 2. 验证用户密码是否正确，正确则继续
        // 3. 构造返回数据

        int returnVal = userService.login(loginReq.getUsername(), loginReq.getPassword());
        if (returnVal == 0) {
            log.warn("...");

            LoginApiProto.LoginResp respBody = LoginApiProto.LoginResp.newBuilder()
                .setId(123456)
                .setToken("")
                .build();

            byte[] messageBytes = MessageProtocol
                .pack(
                    respBody.toByteArray(),
                    "iqq://127.0.0.1:8080/sign_in_resp",
                    MessageHeaderProto.MessageHeader.ContentType.SIGN_IN_RESP,
                    "server",
                    "client",
                    TokenUtil.generate());

            ByteBuffer buffer = ByteBuffer.wrap(messageBytes);
            int len = 0;
            while (buffer.hasRemaining()) {
                try {
                    len += channel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("len" + len);
        } else {
            log.warn("###");
        }
    }
}
