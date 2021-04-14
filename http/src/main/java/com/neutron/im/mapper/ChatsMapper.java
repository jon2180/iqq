package com.neutron.im.mapper;

import com.neutron.im.core.entity.RecentChat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatsMapper {

    List<RecentChat> findByAccountId(String id);

    RecentChat findById(String id);

    boolean update(RecentChat chat);

    int delete(String id);

    int insert(RecentChat chat);
}
