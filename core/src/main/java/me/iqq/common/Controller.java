package me.iqq.common;

import me.iqq.common.protocol.DataPacket;

import java.nio.channels.SocketChannel;

public interface Controller {
    /**
     * 实际处理
     *  @param packet  消息包
     * @param channel 频道
     */
    void handle(DataPacket packet, SocketChannel channel);
}
