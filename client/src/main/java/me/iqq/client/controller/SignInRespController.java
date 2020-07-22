package me.iqq.client.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import me.iqq.client.ui.model.FramesManager;
import me.iqq.common.api.LoginApiProto;
import me.iqq.common.protocol.MessagePacket;

import java.nio.channels.SocketChannel;

@Slf4j
public class SignInRespController extends BaseController {

    public SignInRespController(FramesManager fm, SocketChannel channel) {
        super(fm, channel);
    }

    @Override
    public void handle(MessagePacket packet, SocketChannel channel) {
        log.info(String.valueOf(packet.getHeader().getContentLength()));

        try {
            LoginApiProto.LoginResp loginResp = LoginApiProto.LoginResp.parseFrom(packet.getBody());

            log.info("" + loginResp.getId() + " " + loginResp.getToken());

            int statusCode = packet.getHeader().getStatusCode();

            if (statusCode == 0) {
                framesManager.switchToMain();
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }
}
