package com.neutron.im.controller;

import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import com.neutron.im.core.dto.RequestForm;
import com.neutron.im.core.entity.Request;
import com.neutron.im.service.FriendService;
import com.neutron.im.service.GroupService;
import com.neutron.im.service.RequestService;
import com.neutron.im.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;
    private final FriendService friendService;
    private final GroupService groupService;

    @Autowired
    public RequestController(RequestService requestService,
                             FriendService friendService,
                             GroupService groupService) {
        this.requestService = requestService;
        this.friendService = friendService;
        this.groupService = groupService;
    }

    @GetMapping("/friends/")
    public ResultVO getFriendRequests(@RequestAttribute TokenUtil.JwtClaimsData claims) {
        List<Request> requests = requestService.findByTargetId(Integer.parseInt(claims.getId()));
        return ResultVO.success(requests);
    }

    @PostMapping("/friends/{fid}/req")
    public ResultVO postAddFriendRequest(@PathVariable String fid,
                                         @RequestAttribute TokenUtil.JwtClaimsData claims,
                                         @RequestBody RequestForm.FriendRequestForm requestForm
    ) {
        int val = requestService.insertRequest(claims.getId(), fid, false, requestForm.getReason());
        if (val <= 0) {
            return ResultVO.failed(StatusCode.S403_FORBIDDEN, "发送请求失败", null);
        }
        return ResultVO.success(val);
    }

    @PostMapping("/friends/{fid}/confirm")
    public ResultVO postConfirmFriend(@PathVariable String fid) {
        return ResultVO.success(fid);
    }

    @PostMapping("/groups/{gid}/req")
    public String postEnterGroupRequest(@PathVariable String gid) {
        return gid;
    }

    @PostMapping("/groups/{gid}/confirm")
    public String putEnterGroupConfirm(@PathVariable String gid) {
        return gid;
    }
}
