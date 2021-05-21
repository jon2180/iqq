package com.neutron.im.websocket.handler;

import com.neutron.im.service.MessageService;
import com.neutron.im.websocket.MessageRelayServer;
import com.neutron.im.websocket.WebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultHandler extends BaseHandler {

    @Autowired
    public DefaultHandler(MessageService service) {

    }

    @Override
    public int handle(WebSocketMessage message, MessageRelayServer server) {
        log.warn(message.toString());
        return 0;
    }
}
