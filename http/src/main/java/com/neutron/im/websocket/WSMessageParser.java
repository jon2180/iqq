package com.neutron.im.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WSMessageParser {

    //    public static WebSocketMessage DEFAULT_MESSAGE = new WebSocketMessage();
    private static ObjectMapper mapper = new ObjectMapper();

    WebSocketMessage decode(String message) {
        WebSocketMessage wsMessage = null;

        try {
            wsMessage = mapper.readValue(message, WebSocketMessage.class);
        } catch (JsonMappingException e) {
            log.error("Json映射错误: {}", e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("Json解析错误: {}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (wsMessage == null) {
            log.info("InvalidFormat");
        }

        log.info("esMessage: {}", wsMessage);
        return wsMessage;
    }

    public String encode(WebSocketMessage webSocketMessage) {
        try {
            return mapper.writeValueAsString(webSocketMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
