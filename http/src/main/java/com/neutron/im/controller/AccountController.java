package com.neutron.im.controller;

import com.neutron.im.entity.Account;
import com.neutron.im.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public List<Account> getAll() {
        var data = accountService.findAll();
        return data;
    }

    @GetMapping("/{id}")
    public String getData(@PathVariable String id) {
        if (id != null){
            return accountService.findOneByEmail(id).toString();
        }


        return Objects.requireNonNullElse(id, "Hello World");
    }
}
