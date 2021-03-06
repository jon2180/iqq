package com.neutron.im.mapper;

import com.neutron.im.core.entity.Friend;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendMapper {

    List<Map<String, Object>> findDetailByAccountId(String accountId);

    boolean updateOne(Friend friend);

    int insertOne(Friend friend);

    @Insert("insert into friends(id, `fid_1`, `fid_2`) values(replace(uuid(), '-',''), #{fid1},#{fid2})")
    int insertOneByDefault(String fid1, String fid2);

    @Delete("delete from friends where id = #{id}")
    int deleteOneById(String id);

    Friend findOne(String myId, String id);
}
