package com.neutron.im.mapper;

import com.neutron.im.core.entity.Request;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RequestMapper {
    Request findOneById(String id);

    List<Request> findByOriginatorId(String id);

    List<Request> findByTargetId(String id);

    int insertRequest(Request request);

    boolean updateRequest(Request request);

    int deleteRequest(String id);

    Request findOne(String firstId, String secondId);
}
