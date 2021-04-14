package com.neutron.im.service;

import com.neutron.im.core.entity.Request;
import com.neutron.im.mapper.RequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    public Request findOneById(int id) {
        return requestMapper.findOneById(id);
    }

    public List<Request> findByOriginatorId(int id) {
        return requestMapper.findByOriginatorId(id);
    }

    public List<Request> findByTargetId(int id) {
        return requestMapper.findByTargetId(id);
    }

    public int insertRequest(@NonNull String initiatorId, @NonNull String targetId, @NonNull boolean isGroupRequest, String submitReason) {
//        if (initiatorId == 0 || targetId == 0) {
//            return 0;
//        }

        Request request = new Request() {{
            setInitiator_id(initiatorId);
            setTarget_id(targetId);
            setType(isGroupRequest ? 1 : 0);
            setSubmit_reason(submitReason);
        }};

        return requestMapper.insertRequest(request);
    }

    public boolean updateRequest(Request request) {
        return requestMapper.updateRequest(request);
    }

    public int deleteRequest(int id) {
        return requestMapper.deleteRequest(id);
    }

    private final RequestMapper requestMapper;

    @Autowired
    public RequestService(RequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }
}
