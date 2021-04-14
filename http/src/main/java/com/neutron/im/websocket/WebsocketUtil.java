package com.neutron.im.websocket;

import com.neutron.im.core.entity.Account;
import com.neutron.im.service.AccountService;
import com.neutron.im.service.ChatsService;
import com.neutron.im.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebsocketUtil {

    public static MessageService messageService;
    public static AccountService accountService;
    public static ChatsService chatsService;

    @Autowired
    WebsocketUtil(MessageService service, AccountService rwaAccountService, ChatsService chatsService) {
        WebsocketUtil.messageService = service;
        WebsocketUtil.accountService = rwaAccountService;
        WebsocketUtil.chatsService = chatsService;
    }

    public static void send() {
        List<Account> accounts = accountService.searchFuzzily("i");
        for (var account : accounts) {
            System.out.println(account);
        }
    }
}
