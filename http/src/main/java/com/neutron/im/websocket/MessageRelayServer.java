package com.neutron.im.websocket;

import com.neutron.im.core.entity.Message;
import com.neutron.im.core.entity.RecentChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/relay/{query}")
@Component
public class MessageRelayServer {

    private static final Map<String, Integer> map = new HashMap<>() {{
        put("text", 0);
        put("image", 1);
        put("voice", 2);
        put("video", 3);
        put("other", 4);
    }};

    private static final Map<Integer, String> chatTypeMap = new HashMap<>() {{
        put(0, "single");
        put(1, "group");
//        put(0, "single");
//        put(0, "single");
//        put(0, "single");

    }};


    //    private static CopyOnWriteArraySet<MessageRelayServer> wsSet = new CopyOnWriteArraySet<>();
//    private static AtomicInteger onlineCount = new AtomicInteger(0);
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
     * 服务端发送消息给客户端
     */
    private static void sendMessage(String message, Session session, String toId) {
        try {
//            log.info("服务端给客户端[{}]发送消息{}", id, message);
//            serverMap.get();
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败：{}", e.getMessage());
        }
    }

    /**
     * 群发自定义消息
     */

    public static void broadcast(String message) {
        for (var item : clientMap.entrySet()) {
            //同步异步说明参考：http://blog.csdn.net/who\_is\_xiaoming/article/details/53287691
            //this.session.getBasicRemote().sendText(message);
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
//        messageService.findById("#ffff");
        WebsocketUtil.send();
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

        log.info("服务端收到 #{}的消息: {}", this.id, message);

        switch (message) {
            case "HEARTBEAT":
                send(session, message);
                break;
            case "QUIT":
                clientMap.remove(this.id);
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                WebSocketMessage decodedMessage = messageParser.decode(message);
                handleWebSocketMessage(message, decodedMessage);
                break;
        }
    }

    public void handleWebSocketMessage(String rawMessage, WebSocketMessage decodedMessage) {
        if (decodedMessage == null || decodedMessage.getType() == null || "".equals(decodedMessage.getType())) {
            log.info("未知类型的消息：当作心跳包处理，原路原格式返回");
            send(session, rawMessage);
            return;
        }

        log.info("解析后的消息：{}", decodedMessage);

        switch (decodedMessage.getType()) {
            case "single": {
                MessageRelayServer server = clientMap.get(decodedMessage.getReceiver());
                if (server != null) {
                    log.info("服务端尝试给客户端 #{} 发送消息：{}", id, rawMessage);
                    send(server.session, decodedMessage);
                } else {
                    log.error("Target is offline, message need to be store into databases");
                    // TODO save to databases, waiting for the user online
                }
                saveMessageToDatabase(decodedMessage);
                break;
            }
            case "group": {
                List<Session> group = groupMap.get(decodedMessage.getReceiver());
                if (group != null) {
                    send(group, rawMessage);
                }
                saveMessageToDatabase(decodedMessage);
                break;
            }
            default: {
                log.error("消息类型存在，但服务器暂时不能解析");
            }
        }
    }

    public void saveMessageToDatabase(WebSocketMessage wsMessage) {
        // 1st: check if parameters is valid
//        if (wsMessage == null || wsMessage.getContent() == null || "".equals(wsMessage.getContent())) {
////            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "Invalid Parameters", null);
//            log.error("Message is empty");
//            return;
//        }

//        if (!Objects.equals(this.id, wsMessage.getSender())) {
////            return ResultVO.failed(StatusCode.S403_FORBIDDEN, "Can`t Send Message From Others By Yourself", null);
//            log.error("Message Sender is invalid");
//            return;
//        }

        // 2ed: save wsMessage to database;
        // 2.1 insert chat history
//        Map<String, Object> map = wsMessage.getBody();
//        WebsocketUtil.messageService.insertMessage(new Message() {{
//            setChat_id((String) map.get("chat_id"));
//            setChat_type((Integer) map.get("chat_type"));
//            setSender_id((String)map.get("sender_id"));
//            setReceiver_id((String) map.get("receiver_id"));
//            setContent_type((String) map.getOrDefault(wsMessage.getContent_type(), 4));
//            setContent(wsMessage.getContent());
//            setFile_info(wsMessage.getFile_info());
//            setTime(wsMessage.getTime());
//            setStatus(wsMessage.getStatus());
//        }});

        // 2.2 update recent chat table
//        RecentChat chat = WebsocketUtil.chatsService.findById(wsMessage.getChat_id());
//        if (chat == null) {
//            log.warn("No chat Record");
//            return;
////            return ResultVO.failed(StatusCode.S400_BAD_REQUEST, "No Chat Record", null);
//        }
//        chat.setLast_msg_id(wsMessage.getId());
//        chat.setLast_msg_content(wsMessage.getContent());
//        chat.setLast_msg_time(wsMessage.getTime());
//        chat.setUnread_count(chat.getUnread_count() + 1);
//        boolean val = WebsocketUtil.chatsService.update(chat);
//
//        if (!val) {
//            log.error("Update Chat Failed");
////            return ResultVO.failed(StatusCode.S500_SQL_ERROR, "Update Failed");
//        }

//        // 3rd: relay wsMessage
//        int sendResult = MessageRelayServer.send(wsMessage.getReceiver_id(), new WebSocketMessage() {{
//            setTimestamp(wsMessage.getTime().getTime());
//            setSender(wsMessage.getSender_id());
//            setReceiver(wsMessage.getReceiver_id());
//            // TODO set chat type
//            setType(wsMessage.getChat_type() == 0 ? "single" : "group");
//            setBody(new HashMap<>() {{
//                put("chat_id", wsMessage.getChat_id());
//                put("content", wsMessage.getContent());
//                put("content_type", wsMessage.getContent_type());
//                put("id", wsMessage.getId());
//                put("sender_id", wsMessage.getSender_id());
//                put("receiver_id", wsMessage.getReceiver_id());
//                put("time", wsMessage.getTime().getTime());
//            }});
//        }});

//        log.info("sent result: {}", sendResult);
    }
}
