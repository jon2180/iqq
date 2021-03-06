package me.iqq.common.socket.impl;

import lombok.extern.slf4j.Slf4j;
import me.iqq.common.Controller;
import me.iqq.common.ControllerMapper;
import me.iqq.common.api.MessageHeaderProto;
import me.iqq.common.protocol.DataPacket;
import me.iqq.common.socket.EventDispatcher;

import java.nio.channels.SocketChannel;

/**
 * 统一分发处理路由，此实现需要用到 ControllerMapper 来映射 处理对象，实现请求分发
 *
 * @version 191215
 */
@Slf4j
public class EventDispatcherImpl<T extends Controller> implements EventDispatcher {

    protected ControllerMapper<T> handlerMap;

    public EventDispatcherImpl(ControllerMapper<T> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void dispatch(DataPacket packet, SocketChannel socketChannel) {
        if (handlerMap != null && handlerMap.containsKey(packet.getContentType())) {
            handlerMap.get(packet.getContentType())
                .handle(packet, socketChannel);
        } else {
            log.info("No such message handler");
        }
    }

    private int readHeader(MessageHeaderProto.MessageHeader header) {
        String token = header.getToken();
        if (!verifyToken(token))
            return 1;

        return 0;
    }

    private boolean verifyToken(String token) {
        return true;
    }
}
