package me.iqq.server.handler;

import me.iqq.common.protocol.MessagePacket;

import java.nio.channels.SocketChannel;

public class TextMessageHandler extends MessageHandler {
    @Override
    public void handle() {

    }

    @Override
    public void handle(MessagePacket packet, SocketChannel socketChannel) {
        System.out.println("Header Length: " + packet.getHeaderLength());
        System.out.println("Content Length: " + packet.getHeader().getContentLength());
    }
}
