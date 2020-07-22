package me.iqq.server.model;

import lombok.Builder;
import lombok.Getter;

import java.nio.channels.SocketChannel;

@Builder
@Getter
public class UserSocket {
    /**
     * 用户id
     */
    private final String userID;

    /**
     * 与用户相绑定的通道
     */
    private final SocketChannel channel;
}

