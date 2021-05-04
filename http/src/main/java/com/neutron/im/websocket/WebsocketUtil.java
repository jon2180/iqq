package com.neutron.im.websocket;

import com.neutron.im.core.entity.Account;
import com.neutron.im.core.entity.Message;
import com.neutron.im.core.entity.RecentChat;
import com.neutron.im.service.AccountService;
import com.neutron.im.service.ChatsService;
import com.neutron.im.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WebsocketUtil {
    private static final Map<String, Integer> CONTENT_TYPE_MAP = new HashMap<>() {{
        put("text", 0);
        put("image", 1);
        put("voice", 2);
        put("video", 3);
        put("other", 4);
    }};

    private static final Map<Integer, String> chatTypeMap = new HashMap<>() {{
        put(0, "single");
        put(1, "group");
    }};

    public static MessageService messageService;
    public static AccountService accountService;
    public static ChatsService chatsService;

    @Autowired
    WebsocketUtil(MessageService service, AccountService rwaAccountService, ChatsService chatsService) {
        WebsocketUtil.messageService = service;
        WebsocketUtil.accountService = rwaAccountService;
        WebsocketUtil.chatsService = chatsService;
    }

    public static void send() {
//        List<Account> accounts = accountService.searchFuzzily("i");
//        for (var account : accounts) {
//            System.out.println(account);
//        }
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
            setChat_type(CONTENT_TYPE_MAP.get(decodedMessage.getType()));
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
}
