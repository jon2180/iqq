package com.neutron.im.mapper;

import com.neutron.im.core.entity.Request;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RequestMapper {
    Request findOneById(int id);

    List<Request> findByOriginatorId(int id);

    List<Request> findByTargetId(int id);

    int insertRequest(Request request);

    boolean updateRequest(Request request);

    int deleteRequest(int id);
}
