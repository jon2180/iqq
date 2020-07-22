package me.iqq.client.controller;

import me.iqq.client.ui.model.FramesManager;
import me.iqq.common.protocol.MessagePacket;

import java.nio.channels.SocketChannel;

/**
 * 接收消息
 *
 * @version v200713
 */
public class MessageReceiveController extends BaseController {

    public MessageReceiveController(FramesManager fm, SocketChannel channel) {
        super(fm, channel);
    }

    @Override
    public void handle(MessagePacket packet, SocketChannel channel) {
//        int statusCode = request.getStatusCode();
//        long time = request.getTime();
//        DataType type = request.getType();
//
//        Message message = request.getMessage();
//        ChatFrame chatFrame = framesManager.createChatFrame(message.getOriginAccount());
//        chatFrame.receiveMessage(message, time);
    }
}
