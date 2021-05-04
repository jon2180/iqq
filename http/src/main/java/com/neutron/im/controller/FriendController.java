package com.neutron.im.controller;

import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import com.neutron.im.core.dto.RequestForm;
import com.neutron.im.core.entity.Friend;
import com.neutron.im.service.FriendService;
import com.neutron.im.util.StringUtil;
import com.neutron.im.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService service) {
        this.friendService = service;
    }

    /**
     * 获取朋友的列表
     */
    @GetMapping("/")
    public ResultVO getFriends(@RequestAttribute("claims") TokenUtil.JwtClaimsData claims) {
//        log.info("claims.id: {}", claims.getEmail());
        List<Map<String, Object>> friends = friendService.findDetailsByAccountId(claims.getId());
        return ResultVO.success(friends);
    }

    /**
     * 获取指定朋友的信息
     *
     * @param id fid
     * @return 好友信息
     */
    @PutMapping("/{id}")
    public ResultVO updateFriend(@PathVariable String id,
                                 @RequestAttribute("claims") TokenUtil.JwtClaimsData data,
                                 @RequestBody RequestForm.UpdateFriendForm friendForm
    ) {
        if (StringUtil.isEmpty(id) || StringUtil.isEmpty(data.getId())) {
            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "请指定要更新状态的对象", null);
        }
        Friend friend = friendService.updateFriend(
            data.getId(), id, friendForm.getCategory(), friendForm.getStatus()
        );
        if (friend == null)
            return ResultVO.failed(StatusCode.S404_NOT_FOUND, "没有找到更新的对象", null);
        return ResultVO.success(friend);
    }

    /**
     * 删除好友
     *
     * @param fid 朋友 id
     */
    @DeleteMapping("/{fid}")
    public ResultVO deleteFriend(@PathVariable String fid, @RequestAttribute("claims") TokenUtil.JwtClaimsData data) {
        return ResultVO.success(null);
    }
}
