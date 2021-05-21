package com.neutron.im.controller;

import com.neutron.im.config.AppConstants;
import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import com.neutron.im.core.dto.RequestForm;
import com.neutron.im.core.entity.RecentChat;
import com.neutron.im.service.ChatsService;
import com.neutron.im.service.MessageCheckService;
import com.neutron.im.service.MessageService;
import com.neutron.im.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/chats")
public class ChatsController {

    private final ChatsService chatsService;

    private final MessageService messageService;
    private final MessageCheckService messageCheckService;

    @Autowired
    public ChatsController(ChatsService chatsService, MessageService messageService, MessageCheckService messageCheckService) {
        this.chatsService = chatsService;
        this.messageService = messageService;
        this.messageCheckService = messageCheckService;
    }

    @GetMapping("/")
    public ResultVO getChats(@RequestAttribute("claims") TokenUtil.JwtClaimsData claims) {
        List<Map<String, Object>> chats = chatsService.findByAccountId(claims.getId());
        log.info("chats: {}", chats);
        Map<Object, Object> checkTimeMap = messageCheckService.getEntries(claims.getId());
        log.info("checkTimeMap: {}", checkTimeMap);
        if (checkTimeMap != null) {
            final Date defaultTime = new Date(0L);
            try {
                System.out.println("chatSize:" + chats.size());
                for (Map<String, Object> item : chats) {
                    if (item.containsKey("account_avatar")) {
                        item.replace("account_avatar", AppConstants.getAvatarUrl((String) item.get("account_avatar")));
                    }
                    String chatId = (String) item.get("id");
                    System.out.println("chat_id" + chatId);
                    System.out.println(checkTimeMap);
                    if (checkTimeMap.containsKey(chatId)) {
                        long latestCheckTime = Long.parseLong((String) checkTimeMap.get(chatId));
                        long count = messageService.countAfter(chatId, latestCheckTime);
                        System.out.println("checked" + latestCheckTime + " " + count);
                        item.replace("unread_count", count);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVO.failed(StatusCode.S500_LOGIC_ERROR, "Search Failed");
            }
            System.out.println("checkTimeMap is not null");
        } else {
            System.out.println("checkTimeMap is  null");
        }

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
