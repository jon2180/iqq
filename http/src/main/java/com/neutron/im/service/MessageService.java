package com.neutron.im.service;

import com.neutron.im.core.entity.Message;
import com.neutron.im.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {
    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageMapper mapper) {
        this.messageMapper = mapper;
    }

    public List<Message> findBySenderAndReceiver(String id1, String id2) {
        return messageMapper.findBySenderAndReceiver(id1, id2);
    }

    public int insertMessage(Message message) {
        return messageMapper.insert(message);
    }

    public List<Message> findById(String id) {
        return messageMapper.findByChatId(id);
    }

    public long countAfter(String chatId, long time) {
        Map<String, Object> result = messageMapper.countAfter(chatId, new Timestamp(time));
        try {
            return (Long) result.getOrDefault("count", 0L);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
