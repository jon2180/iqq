package com.neutron.im.mapper;

import com.neutron.im.core.entity.RecentChat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChatsMapper {

    List<Map<String, Object>> findByAccountId(String id);

    RecentChat findById(String id);

    boolean update(RecentChat chat);

    int delete(String id);

    int insert(RecentChat chat);
}
