package com.neutron.im.websocket.handler;

import com.neutron.im.websocket.MessageRelayServer;
import com.neutron.im.websocket.WebSocketMessage;
import com.neutron.im.websocket.WebsocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class SingleChatHandler extends BaseHandler {

    public int handle(WebSocketMessage message, MessageRelayServer server) {
        MessageRelayServer targetConn = MessageRelayServer.clientMap.get(message.getReceiver());
        if (!Objects.equals(server.getId(), message.getSender())) {
            log.error("Message Sender is invalid");
            return 1;
        }
        // 如果对方在线，转发消息
        if (targetConn != null) {
            MessageRelayServer.send(targetConn.getId(), message);
        }
        // 保存至数据库
        try {
            WebsocketUtil.saveMessageToDatabase(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
