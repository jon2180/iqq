package me.iqq.common;

import me.iqq.common.protocol.MessagePacket;

import java.nio.channels.SocketChannel;

public abstract class Controller {
    /**
     * 实际处理
     *
     * @param packet  消息包
     * @param channel 频道
     */
    public abstract void handle(MessagePacket packet, SocketChannel channel);
}
