package com.neutron.im.controller;

import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import com.neutron.im.core.dto.RequestForm;
import com.neutron.im.core.entity.Message;
import com.neutron.im.core.entity.RecentChat;
import com.neutron.im.service.ChatsService;
import com.neutron.im.service.MessageService;
import com.neutron.im.util.TokenUtil;
import com.neutron.im.websocket.MessageRelayServer;
import com.neutron.im.websocket.WebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/messages")
public class MessageController {

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
    }};

    private final MessageService messageService;
    private final ChatsService chatsService;

    @Autowired
    public MessageController(MessageService service, ChatsService chatsService) {
        this.messageService = service;
        this.chatsService = chatsService;
    }

    @GetMapping("/{id}")
    public ResultVO getChatHistory(@PathVariable String id, @RequestAttribute TokenUtil.JwtClaimsData claims) {
//        List<Message> messages = messageService.findBySenderAndReceiver(claims.getId(), id);
        List<Message> messages = messageService.findById(id);
        return ResultVO.success(messages);
    }

    @PostMapping("/")
    public ResultVO postChatHistory(
        @RequestBody RequestForm.MessageSaveForm message,
        @RequestAttribute TokenUtil.JwtClaimsData claims) {
        // 1st: check if parameters is valid
        if (message == null || message.getContent() == null || "".equals(message.getContent())) {
            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "Invalid Parameters", null);
        }

        if (!Objects.equals(claims.getId(), message.getSender_id())) {
            return ResultVO.failed(StatusCode.S403_FORBIDDEN, "Can`t Send Message From Others By Yourself", null);
        }

        // 2ed: save message to database;
        // 2.1 insert chat history
        messageService.insertMessage(new Message() {{
            setChat_id(message.getChat_id());
            setChat_type(message.getChat_type());
            setSender_id(message.getSender_id());
            setReceiver_id(message.getReceiver_id());
            setContent_type(map.getOrDefault(message.getContent_type(), 4));
            setContent(message.getContent());
            setFile_info(message.getFile_info());
            setTime(message.getTime());
            setStatus(message.getStatus());
        }});

        // 2.2 update recent chat table
        RecentChat chat = chatsService.findById(message.getChat_id());
        if (chat == null) {
            return ResultVO.failed(StatusCode.S400_BAD_REQUEST, "No Chat Record", null);
        }
        chat.setLast_msg_id(message.getId());
        chat.setLast_msg_content(message.getContent());
        chat.setLast_msg_time(message.getTime());
        chat.setUnread_count(chat.getUnread_count() + 1);
        boolean val = chatsService.update(chat);

        if (!val) {
            return ResultVO.failed(StatusCode.S500_SQL_ERROR, "Update Failed");
        }

        // 3rd: relay message
        int sendResult = MessageRelayServer.send(message.getReceiver_id(), new WebSocketMessage() {{
            setTimestamp(message.getTime().getTime());
            setSender(message.getSender_id());
            setReceiver(message.getReceiver_id());
            // TODO set chat type
            setType(message.getChat_type() == 0 ? "single" : "group");
            setBody(new HashMap<>() {{
                put("chat_id", message.getChat_id());
                put("content", message.getContent());
                put("content_type", message.getContent_type());
                put("id", message.getId());
                put("sender_id", message.getSender_id());
                put("receiver_id", message.getReceiver_id());
                put("time", message.getTime().getTime());
            }});
        }});

        log.info("sent result: {}", sendResult);

        // 4th: response to client
        return ResultVO.success(null);
    }
}
