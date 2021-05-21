package com.neutron.im.websocket;

import com.neutron.im.core.entity.Message;
import com.neutron.im.core.entity.RecentChat;
import com.neutron.im.service.AccountService;
import com.neutron.im.service.ChatsService;
import com.neutron.im.service.MessageService;
import com.neutron.im.websocket.handler.BaseHandler;
import com.neutron.im.websocket.handler.DefaultHandler;
import com.neutron.im.websocket.handler.GroupChatHandler;
import com.neutron.im.websocket.handler.SingleChatHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebsocketUtil {

    /**
     * 消息内容类型
     */
    private static final Map<String, Integer> CONTENT_TYPE_MAP = new HashMap<>() {{
        put(ChatMessageType.TEXT.getData(), 0);
        put(ChatMessageType.IMAGE.getData(), 1);
        put(ChatMessageType.AUDIO.getData(), 2);
        put(ChatMessageType.VIDEO.getData(), 3);
        put(ChatMessageType.CODE_SNIPS.getData(), 3);
        put(ChatMessageType.FAVORITE.getData(), 3);
        put("other", 4);
    }};
    private static final Map<Integer, String> chatTypeMap = new HashMap<>() {{
        put(0, "single");
        put(1, "group");
    }};
    private static final Map<String, Integer> chatTypeToId = new HashMap<>() {{
        put("single", 0);
        put("group", 1);
    }};
    public static ConcurrentHashMap<String, BaseHandler> handlersMap = new ConcurrentHashMap<>();
    public static DefaultHandler defaultHandler;
    public static MessageService messageService;
    public static AccountService accountService;
    public static ChatsService chatsService;

    @Autowired
    WebsocketUtil(
        MessageService service,
        AccountService rwaAccountService,
        ChatsService chatsService,
        DefaultHandler defaultHandler,
        SingleChatHandler singleChatHandler,
        GroupChatHandler groupChatHandler
    ) {
        WebsocketUtil.messageService = service;
        WebsocketUtil.accountService = rwaAccountService;
        WebsocketUtil.chatsService = chatsService;
        WebsocketUtil.defaultHandler = defaultHandler;
        handlersMap.put("single", singleChatHandler);
        handlersMap.put("group", groupChatHandler);
    }

    public static void saveMessageToDatabase(WebSocketMessage decodedMessage) {
        // 1st: check if parameters is valid
        if (decodedMessage == null || decodedMessage.getType() == null || decodedMessage.getBody() == null) {
            log.error("Message is empty");
            return;
        }

        // 2ed: save decodedMessage to database;
        // 2.1 insert chat history
        Map<String, Object> messageBody = decodedMessage.getBody();
        Message message = new Message() {{
            setChat_id((String) messageBody.get("chat_id"));
            // FIXME 会话类型
            setChat_type(chatTypeToId.get(decodedMessage.getType()));
            setSender_id((String) messageBody.get("sender_id"));
            setReceiver_id((String) messageBody.get("receiver_id"));
            setContent_type(CONTENT_TYPE_MAP.getOrDefault((String) messageBody.get("content_type"), 4));
            setContent((String) messageBody.getOrDefault("content", ""));
            setFile_info((String) messageBody.getOrDefault("file_info", ""));
            setTime(new Date(decodedMessage.getTimestamp()));
            setStatus(0);
        }};

        if (!(message.getChat_id() == null || message.getSender_id() == null || message.getReceiver_id() == null)) {
            messageService.insertMessage(message);
        }

        // 2.2 update recent chat table
        RecentChat chat = chatsService.findById((String) messageBody.get("chat_id"));
        if (chat == null) {
            log.warn("No chat Record");
//            chatsService.insertByDefault();
        } else {
            chat.setLast_msg_id((String) messageBody.get("id"));
            chat.setLast_msg_content((String) messageBody.get("content"));
            chat.setLast_msg_time(new Date((long) messageBody.get("time")));
            chat.setUnread_count(chat.getUnread_count() + 1);
            boolean val = WebsocketUtil.chatsService.update(chat);
            if (!val) {
                log.error("Update Chat Failed");
            }
        }
    }

    public enum ChatMessageType {
        TEXT("text"),
        IMAGE("image"),
        AUDIO("audio"),
        VIDEO("video"),
        CODE_SNIPS("codesnips"),
        FAVORITE("favorite");

        private final String data;

        ChatMessageType(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    public enum MessageType {
        SINGLE("single"),
        GROUP("group");

        private final String data;

        MessageType(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }
}
