package com.neutron.im.websocket.handler;

import com.neutron.im.websocket.MessageRelayServer;
import com.neutron.im.websocket.WebSocketMessage;


public abstract class BaseHandler {

    public abstract int handle(WebSocketMessage message, MessageRelayServer server);
}
