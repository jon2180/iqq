package com.neutron.im.service;

import com.neutron.im.core.entity.Message;
import com.neutron.im.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
