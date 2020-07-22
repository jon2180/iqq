package me.iqq.server.handler;

import me.iqq.common.protocol.MessagePacket;

import java.nio.channels.SocketChannel;

public abstract class MessageHandler {
    public abstract void handle();

    public void broadcast() {
        // TODO 群聊功能
    }

    public abstract void handle(MessagePacket packet, SocketChannel socketChannel);
}
