package com.neutron.im.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendMapper {
    @Insert("insert into friends(`fid_1`, `fid_2`) values(#{fid1},#{fid2})")
    int insert(int fid1, int fid2);

    @Delete("delete from friends where id = #{id}")
    int delete(int id);


}
