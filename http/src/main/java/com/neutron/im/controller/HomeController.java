package com.neutron.im.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neutron.im.config.MailService;
import com.neutron.im.core.ResultVO;
import com.neutron.im.core.StatusCode;
import com.neutron.im.core.dto.RequestForm;
import com.neutron.im.core.dto.RequestForm.LoginForm;
import com.neutron.im.core.entity.Account;
import com.neutron.im.core.exception.AuthFailedException;
import com.neutron.im.core.exception.DisabledAccountException;
import com.neutron.im.core.exception.NoSuchAccountException;
import com.neutron.im.service.AccountService;
import com.neutron.im.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RestController
@RequestMapping("/")
@Slf4j
public class HomeController {

    private final AccountService accountService;
    private final RedisUtil redisUtil;
    private final ObjectMapper mapper;
    private final MailService mailService;

    @Autowired
    public HomeController(
        AccountService accountService,
        RedisUtil redisUtil,
        ObjectMapper mapper,
        MailService mailService
    ) {
        this.accountService = accountService;
        this.redisUtil = redisUtil;
        this.mapper = mapper;
        this.mailService = mailService;
    }

    @GetMapping("/")
    public ResultVO index() {
        return ResultVO.success("This is index page");
    }

    /**
     * POST /login
     * 登录
     *
     * @param loginForm 登录表单数据
     * @param response  响应对象
     * @return 登录成功就返回实体内容
     */
@PostMapping("/login")
public ResultVO postLogin(@RequestBody LoginForm loginForm, HttpServletResponse response, HttpSession session) {
    // check format
    if (StringUtil.isEmpty(loginForm.getEmail()) || !Validator.isEmail(loginForm.getEmail())) {
        return ResultVO.failed(StatusCode.S400_INVALID_PARAMETERS_FORMAT, "邮箱格式验证失败，请确认后重新登录", null);
    }
    if (StringUtil.isEmpty(loginForm.getPassword()) || !Validator.isPassword(loginForm.getPassword())) {
        return ResultVO.failed(StatusCode.S400_INVALID_PARAMETERS_FORMAT, "密码格式验证失败，请确认后重新登录", null);
    }
    String captchaInSession = session.getAttribute("captcha-pic") != null
        ? String.valueOf(session.getAttribute("captcha-pic")).toLowerCase()
        : "";
    log.info("captcha: {}", captchaInSession);
    if (StringUtil.isEmpty(captchaInSession)) {
        return ResultVO.failed(StatusCode.S400_INVALID_CAPTCHA, "请先获取验证码并在其有效时间段内提交验证", null);
    }
    String captcha = loginForm.getCaptcha() != null
        ? loginForm.getCaptcha().toLowerCase()
        : "";
    if (StringUtil.isEmpty(captcha) || !captcha.equals(captchaInSession)) {
        return ResultVO.failed(StatusCode.S400_INVALID_CAPTCHA, "验证码验证失败", null);
    }
    try {
        Account result = accountService.loginCheck(loginForm.getEmail(), loginForm.getPassword());
        response.addCookie(
            new Cookie(
                TokenUtil.AUTHORIZATION,
                TokenUtil.generateTokenWithPrefix(new TokenUtil.JwtClaimsData() {{
                    setId(result.getId());
                }})
            ) {{
                setMaxAge(TokenUtil.EXPIRATION_TIME_IN_SECOND);
            }}
        );
        return ResultVO.success(result);
    } catch (NoSuchAccountException e) {
        return ResultVO.failed(StatusCode.S401_NO_SUCH_ACCOUNT, e.getMessage(), null);
    } catch (AuthFailedException e) {
        return ResultVO.failed(StatusCode.S401_UNMATCHED_PRIVATE_KEY, e.getMessage(), null);
    } catch (DisabledAccountException e) {
        return ResultVO.failed(StatusCode.S401_DISABLED_ACCOUNT, e.getMessage(), null);
    } catch (Exception e) {
        return ResultVO.failed(StatusCode.S500_INTERNAL_SERVER_ERROR, e.getMessage(), null);
    }
}

    /**
     * POST /accounts
     * 用户注册
     *
     * @param form 注册参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResultVO postRegister(@RequestBody RequestForm.RegisterForm form) {
        // check format
        if (StringUtil.isEmpty(form.getEmail()) || !Validator.isEmail(form.getEmail())) {
            return ResultVO.failed(StatusCode.S400_INVALID_PARAMETERS_FORMAT, "邮箱格式验证失败，请确认后重新登录", null);
        }
        if (StringUtil.isEmpty(form.getNickname()) || "".equals(form.getNickname())) {
            return ResultVO.failed(StatusCode.S400_INVALID_PARAMETERS_FORMAT, "用户名格式验证失败，请确认后重新登录", null);
        }
        if (StringUtil.isEmpty(form.getPassword()) || !Validator.isPassword(form.getPassword())) {
            return ResultVO.failed(StatusCode.S400_INVALID_PARAMETERS_FORMAT, "密码格式验证失败，请确认后重新登录", null);
        }
        int res = accountService.doRegister(form.getEmail(), form.getPassword(), form.getNickname());

        String message = "";
        switch (res) {
            case 0: {
                message = "OK";
                break;
            }
            case 1: {
                message = "注册失败，可能是服务器内部错误";
                break;
            }
            case 2: {
                message = "邮箱被占用";
                break;
            }
            case 3: {
                message = "密码或邮箱格式错误";
                break;
            }
            default: {
                message = "服务器内部错误";
            }
        }

        return ResultVO.success(null).setMessage(message);
    }

    /**
     * GET /captcha-pic
     * 发送图片验证码，用于接收邮件验证码之前
     * 1. 生成验证码
     * 2. 把验证码上的文本存在session中
     * 3. 把验证码图片发送给客户端
     */
    @GetMapping("/captcha-pic")
    public void getCaptchaPic(HttpServletResponse response, HttpSession session) throws IOException {
        // 用我们的验证码类，生成验证码类对象
        CaptchaGeneratorUtil generator = new CaptchaGeneratorUtil();
        // 获取验证码
        BufferedImage image = generator.getImage();
        String text = generator.getText();
        // 将验证码的文本存在session中
        session.setAttribute("captcha-pic", text);
        response.setContentType("image/jpeg");
        // 将验证码图片响应给客户端
        CaptchaGeneratorUtil.output(image, response.getOutputStream());
    }

    /**
     * GET /captcha-email
     *
     * @return 发送结果
     */
    @PostMapping("/captcha-email")
    public ResultVO getCaptchaEmail(HttpSession session) {
        // TODO 发邮箱
        String captchaInSession = String.valueOf(session.getAttribute("captcha-pic"));
        log.info(captchaInSession);
//        mailService.sendSimpleMail(ss"jon2180@outlook.com", "Test Email Feature", "Test Email Feature");
        return ResultVO.success(null);
    }

    @PostMapping("/upload/{type}")
    public ResultVO uploadImage(@PathVariable String type, @RequestParam("file") MultipartFile[] files) {
        if (files == null) {
            return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, "No Files to Upload", null);
        }
        String filePath = "D:/Downloads/";
        File tempDir = new File(filePath);
        if (!tempDir.exists()) return ResultVO.failed(StatusCode.S500_FILE_STORAGE_ERROR, "InvalidTempDirectory", null);

        String[] messages = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file.isEmpty()) {
                messages[i] = "上传第" + (i + 1) + "文件失败, 文件为空";
                continue;
            }
            String fileName = file.getOriginalFilename();

            File dest = new File(filePath + fileName);
            try {
                file.transferTo(dest);
                messages[i] = ("第" + (i + 1) + "个文件上传成功");
            } catch (IOException e) {
                log.error(e.toString(), e);
                messages[i] = "上传第" + (i++) + "个文件失败，转换失败";
            }
        }
        log.info(Arrays.toString(messages));
        return ResultVO.success(messages);
    }
}
