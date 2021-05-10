package com.neutron.im.controller;

import com.neutron.im.config.AppConstants;
import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import com.neutron.im.core.entity.Account;
import com.neutron.im.service.AccountService;
import com.neutron.im.util.StringUtil;
import com.neutron.im.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Account account = accountService.findByID(claims.getId());
        if (account.getAvatar() != null && !"".equals(account.getAvatar())) {
            account.setAvatar(AppConstants.getAvatarUrl(account.getAvatar()));
        }
        return ResultVO.success(account);
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
        if (account.getAvatar() != null) {
            account.setAvatar(AppConstants.getAvatarUrl(account.getAvatar()));
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
     * @return
     */
    @PutMapping("/")
    public ResultVO putAccountInfo(
        @RequestAttribute TokenUtil.JwtClaimsData claims,
        @RequestBody Map<String, Object> map
    ) {
        final String nickname = (String) map.getOrDefault("nickname", "");
        final String signature = (String) map.getOrDefault("signature", "");
        final String gender = (String) map.getOrDefault("gender", "secret");
        final String birthday = (String) map.getOrDefault("birthday", "");

        final Account account = accountService.findByID(claims.getId());
        if (!StringUtil.isEmpty(nickname))
            account.setNickname(nickname);
        if (!StringUtil.isEmpty(signature))
            account.setSignature(signature);
        if (!StringUtil.isEmpty(gender))
            System.out.println(gender);
        if (!StringUtil.isEmpty(birthday))
            System.out.println(birthday);

        return accountService.updateAccountById(account)
            ? ResultVO.success("OK")
            : ResultVO.failed(StatusCode.S400_BAD_REQUEST, "更新出错", null);
    }

    @PostMapping("/avatar")
    public ResultVO updateAvatar(
        MultipartFile file,
        @RequestAttribute TokenUtil.JwtClaimsData claims
    ) {
        if (file == null || file.isEmpty()) {
            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "No Avatar to Upload", null);
        }
        String filePath = AppConstants.AVATAR_STORAGE_PATH;
        File tempDir = new File(filePath);
        if (!tempDir.exists() && !tempDir.mkdir()) {
            return ResultVO.failed(StatusCode.S500_FILE_STORAGE_ERROR, "InvalidTempDirectory", null);
        }

        HashMap<String, Object> responseData = new HashMap<>();
        String message = null;
        String fileName = claims.getId() + "-" + StringUtil.generate(8) + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        try {
            file.transferTo(new File(filePath + "/" + fileName));
            message = ("文件上传成功");
        } catch (IOException e) {
            log.error(e.toString(), e);
            message = "文件失败，转换失败";
        }
        responseData.put("message", message);
        responseData.put("url", AppConstants.getAvatarUrl(fileName));

        Account account = accountService.findByID(claims.getId());
        account.setAvatar(fileName);
        accountService.updateAccountById(account);

        return ResultVO.success(responseData);
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
