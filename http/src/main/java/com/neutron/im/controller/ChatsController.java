package com.neutron.im.controller;

import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import com.neutron.im.core.dto.RequestForm;
import com.neutron.im.core.entity.RecentChat;
import com.neutron.im.service.ChatsService;
import com.neutron.im.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chats")
public class ChatsController {

    private final ChatsService chatsService;

    @Autowired
    public ChatsController(ChatsService chatsService) {
        this.chatsService = chatsService;
    }

    @GetMapping("/")
    public ResultVO getChats(@RequestAttribute("claims") TokenUtil.JwtClaimsData claims) {
        List<Map<String, Object>> chats = chatsService.findByAccountId(claims.getId());
        return ResultVO.success(chats);
    }

    @GetMapping("/{id}")
    public ResultVO getChat(
        @PathVariable String id,
        @RequestAttribute("claims") TokenUtil.JwtClaimsData claims
    ) {
        if (id == null || "".equals(id)) {
            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "No Parameter: id", null);
        }
        RecentChat chat = chatsService.findById(id);
        if (chat == null || chat.getId() == null || "".equals(chat.getId())) {
            return ResultVO.failed(StatusCode.S404_NOT_FOUND, "No Specific Chat", null);
        }
        if (!claims.getId().equals(chat.getAccount_id())) {
            return ResultVO.failed(StatusCode.S403_FORBIDDEN, "No Authorization", null);
        }
        return ResultVO.success(chat);
    }

    @PostMapping("/")
    public ResultVO postChats(
        @RequestAttribute("claims") TokenUtil.JwtClaimsData claims,
        @RequestBody RequestForm.ChatsRequestForm body
    ) {
        if (body == null) {
            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "请指定参数", null);
        }

        int insertRow = chatsService.insertByDefault(new RecentChat() {{
            setAccount_id(claims.getId());
        }});
        if (insertRow == 0) {
//            return ResultVO.failed(S500)
        }

        return ResultVO.success(null);
    }

    @DeleteMapping("/{id}")
    public ResultVO deleteChats(
        @PathVariable String id,
        @RequestAttribute("claims") TokenUtil.JwtClaimsData claims
    ) {
        return ResultVO.failed(StatusCode.S500_LOGIC_ERROR, "NotImplemented", null);
    }
}
