package com.neutron.im.websocket;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@ToString
@Data
public class WebSocketMessage {
    private String sender;
    private String receiver;
    private long timestamp;
    private String type;
    private Map<String, Object> body;
}
