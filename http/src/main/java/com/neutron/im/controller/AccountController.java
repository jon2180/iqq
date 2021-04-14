package com.neutron.im.controller;

import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import com.neutron.im.core.entity.Account;
import com.neutron.im.service.AccountService;
import com.neutron.im.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 获取自己的信息
     *
     * @return 用户列表
     */
    @GetMapping("/")
    public ResultVO getAll(@RequestAttribute TokenUtil.JwtClaimsData claims) {
        return ResultVO.success(accountService.findByID(claims.getId()));
    }

    /**
     * 获取用户信息
     *
     * @param id
     * @param data
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO getAccountInfo(@PathVariable String id, @RequestAttribute("claims") TokenUtil.JwtClaimsData data) {
        if (id == null) {
            log.info("Token Claims: {}", data);
            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "Empty Parameter: Account Id", null);
        }
        Account account = accountService.findByID(id);
        if (account == null) {
            return ResultVO.failed(StatusCode.S404_NOT_FOUND, "No Specific Account", null);
        }
        return ResultVO.success(account);
    }

    /**
     * 删除用户
     *
     * @param claims token 携带数据
     * @return
     */
    @DeleteMapping("/")
    public ResultVO deleteAccount(@RequestAttribute TokenUtil.JwtClaimsData claims) {
        return accountService.deleteAccountById(claims.getId())
            ? ResultVO.success("OK")
            : ResultVO.failed(40001, "Delete Failed", null);
    }

    /**
     * 更新账户信息
     *
     * @param id
     * @return
     */
    @PutMapping("/")
    public ResultVO putAccountInfo(
        @RequestAttribute TokenUtil.JwtClaimsData claims,
        @RequestBody Account rawAccount
    ) {
        return accountService.updateAccountById(rawAccount)
            ? ResultVO.success("OK")
            : ResultVO.failed(StatusCode.S400_BAD_REQUEST, "更新出错", null);
    }

    @GetMapping("/search")
    public ResultVO searchFriend(@RequestParam("keyword") String keyword,
                                 @RequestParam("type") String type,
                                 @RequestAttribute("claims") TokenUtil.JwtClaimsData data) {
        if (keyword != null && !"".equals(keyword)) {
            List<Account> accounts = accountService.searchFuzzily(keyword);
            return ResultVO.success(accounts);
        }
        return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "BadRequest: Empty Parameter", null);
    }
}
