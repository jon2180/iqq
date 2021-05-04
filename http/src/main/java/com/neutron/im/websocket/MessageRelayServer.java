package com.neutron.im.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 入口类，通过 ServerEndPoint 指定了连接路径
 *
 * @version v20210418
 * @since 11
 */
@Slf4j
@ServerEndpoint("/relay/{query}")
@Component
public class MessageRelayServer {

    private static final WSMessageParser messageParser = new WSMessageParser();
    private static final ConcurrentHashMap<String, MessageRelayServer> clientMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, List<Session>> groupMap = new ConcurrentHashMap<>();

    /**
     * z
     * 记录当前在线连接数
     */
    private Session session;
    private String id;
    /**
     * 用于解决 心跳
     */
    private long lastReceiveTime;

    /**
     * 用于单聊，发送消息给客户端
     *
     * @param session 客户端会话 session
     * @param message 消息体
     */
    private static boolean send(Session session, String message) {
        if (!session.isOpen()) {
            log.info("客户端已关闭连接");
            return false;
        }
        boolean success = false;
        try {
            session.getBasicRemote().sendText(message);
            success = true;
        } catch (IOException e) {
            log.error("服务端发送消息给客户端失败：{}", e.getMessage());
            e.printStackTrace();
        }
        return success;
    }

    private static boolean send(Session session, WebSocketMessage message) {
        return send(session, messageParser.encode(message));
    }

    private static int send(List<Session> sessionList, String message) {
        if (sessionList == null) {
            log.error("IllegalArguments: Session Not Null");
            return 0;
        }
        int sentCount = 0;
        for (Session session : sessionList) {
            if (session.isOpen() && send(session, message)) {
                sentCount++;
            }
        }
        return sentCount;
    }

    private static int send(List<Session> sessionList, WebSocketMessage message) {
        return send(sessionList, messageParser.encode(message));
    }

    public static int send(String chatId, String message) {
        MessageRelayServer messageClient = clientMap.get(chatId);
        if (messageClient == null) {
            return 3;
        }
        if (messageClient.session == null || !messageClient.session.isOpen()) {
            return 2;
        }
        return send(messageClient.session, message) ? 0 : 1;
    }

    public static int send(String chatId, WebSocketMessage message) {
        return send(chatId, messageParser.encode(message));
    }

    /**
     * 广播消息
     * 发送至每一个在线用户
     */
    public static void broadcast(String message) {
        for (var item : clientMap.entrySet()) {
            //同步异步说明参考：http://blog.csdn.net/who\_is\_xiaoming/article/details/53287691
            item.getValue().session.getAsyncRemote().sendText(message);//异步发送消息.
        }
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("query") String param) {
        if (param == null || param.equals("") || param.equals("-1")) {
            log.error("客户端没有关键参数传入，将关闭连接");
            try {
                if (session != null)
                    session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        this.id = param;
        this.session = session;
        this.lastReceiveTime = System.currentTimeMillis();
        clientMap.put(param, this);

        log.info("session #{} param: {}", session.getId(), param);
        log.info("有新连接 #{} 加入：当前在线人数为：{}", param, clientMap.size());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("有一连接发生错误: {},当前在线人数为 {}", this.id, clientMap.size());
        error.printStackTrace();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        clientMap.remove(id);
        try {
            if (session != null)
                session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("有一连接关闭：{}，当前在线人数为：{}", id, clientMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        if (message == null || message.equals("")) {
            log.warn("没有收到有效数据");
            return;
        }

        lastReceiveTime = System.currentTimeMillis();

        log.info("服务端收到 #{}的消息: {}", this.id, message);

        // 检查是不是特殊指令
        switch (message) {
            case "PING":
            case "PONG":
                // 心跳响应
                send(session, "PONG");
                break;
            case "QUIT":
                // 服务端断开连接
                clientMap.remove(this.id);
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                // 默认情况应该是用 JSON 格式处理数据
                handleWebSocketMessage(message);
                break;
        }
    }

    public void handleWebSocketMessage(String rawMessage) {
        WebSocketMessage decodedMessage = messageParser.decode(rawMessage);

        if (decodedMessage == null || decodedMessage.getType() == null || "".equals(decodedMessage.getType())) {
            log.info("未知类型的消息：当作心跳包处理，原路原格式返回");
            send(session, "Invalid Format: No Invalid Message Type");
            return;
        }

        switch (decodedMessage.getType()) {
            case "single":
                MessageRelayServer server = clientMap.get(decodedMessage.getReceiver());
                if (server != null) {
                    //        if (!Objects.equals(this.id, decodedMessage.getSender())) {
                    ////            return ResultVO.failed(StatusCode.S403_FORBIDDEN, "Can`t Send Message From Others By Yourself", null);
                    //            log.error("Message Sender is invalid");
                    //            return;
                    //        }

                    log.info("服务端尝试给客户端 #{} 发送消息：{}", id, rawMessage);
                    send(server.session, decodedMessage);
                } else {
                    log.warn("Target is offline, message need to be store into databases");
                    // TODO save to databases, waiting for the user online or cache to redis
                }
                try {
                    WebsocketUtil.saveMessageToDatabase(decodedMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "group":
                List<Session> group = groupMap.get(decodedMessage.getReceiver());
                if (group != null) {
                    send(group, rawMessage);
                }
                WebsocketUtil.saveMessageToDatabase(decodedMessage);
                break;
            default:
                log.error("消息类型存在，但服务器暂时不能解析");
        }
    }
}
