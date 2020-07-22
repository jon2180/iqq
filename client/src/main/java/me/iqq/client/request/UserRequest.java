package me.iqq.client.request;

import lombok.extern.slf4j.Slf4j;
import me.iqq.client.controller.BaseController;
import me.iqq.client.ui.model.FramesManager;
import me.iqq.common.api.LoginApiProto;
import me.iqq.common.api.MessageHeaderProto;
import me.iqq.common.protocol.MessagePacket;
import me.iqq.common.protocol.MessageProtocol;

import java.nio.channels.SocketChannel;

@Slf4j
public class UserRequest extends BaseController {


    public UserRequest(FramesManager fm, SocketChannel channel) {
        super(fm, channel);
    }

    @Override
    public void handle(MessagePacket packet, SocketChannel channel) {

    }


    public void login(String id, String rawPassword) {

        // TODO
        // Validate the id and the password

        // TODO
        // Encrypted the password with hash

        // Construct the Login Object
        LoginApiProto.LoginReq req = LoginApiProto.LoginReq.newBuilder()
            .setUsername(id)
            .setPassword(rawPassword)
            .setIsNeedKey(true)
            .build();

        byte[] bytes = MessageProtocol.pack(
            req.toByteArray(),
            "iqq://localhost:8080/",
            MessageHeaderProto.MessageHeader.ContentType.SIGN_IN,
            "1234567",
            "2346578");

        int returnVal = write(bytes);
        if (returnVal > 0) {
            log.info("Send login Req successfully");
        } else {
            log.info("Send login Req failed");
        }
    }

    public void logon(String id, String rawPassword, String confirmedPassword) {


    }

}