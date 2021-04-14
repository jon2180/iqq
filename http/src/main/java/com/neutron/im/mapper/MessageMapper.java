package com.neutron.im.mapper;

import com.neutron.im.core.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    List<Message> findBySenderAndReceiver(String id1, String id2);

    int insert(Message message);

    List<Message> findByChatId(String chatId);
}
