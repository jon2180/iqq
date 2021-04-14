package com.neutron.im.service;

import com.neutron.im.core.entity.RecentChat;
import com.neutron.im.mapper.ChatsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatsService {

    private final ChatsMapper chatsMapper;

    public ChatsService(ChatsMapper chatsMapper) {
        this.chatsMapper = chatsMapper;
    }

    public List<RecentChat> findByAccountId(String id) {
        return chatsMapper.findByAccountId(id);
    }

    public RecentChat findById(String id) {
        return chatsMapper.findById(id);
    }

    public boolean update(RecentChat chat) {
        return chatsMapper.update(chat);
    }

    public int deleteById(String id) {
        return chatsMapper.delete(id);
    }

    public int insertByDefault(RecentChat recentChat) {
        return chatsMapper.insert(recentChat);
    }
}
