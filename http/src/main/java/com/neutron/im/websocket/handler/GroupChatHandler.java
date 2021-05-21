package com.neutron.im.websocket.handler;

import com.neutron.im.websocket.MessageRelayServer;
import com.neutron.im.websocket.WebSocketMessage;
import org.springframework.stereotype.Component;

@Component
public class GroupChatHandler extends BaseHandler{
    @Override
    public int handle(WebSocketMessage message, MessageRelayServer server) {
        return 0;
    }
}
