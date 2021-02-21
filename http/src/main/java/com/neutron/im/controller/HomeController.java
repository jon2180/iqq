package com.neutron.im.controller;

import com.neutron.im.controller.dto.RequestForm.*;
import com.neutron.im.util.Validator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "This is index page";
    }

    /**
     *
     *
     * @param email
     * @param password
     * @return
     */
    /**
     * @RequestParam 请求参数 Get 的 urlEncoded 形式
     */
    @PostMapping("/login")
    @ResponseBody
    public LoginForm postLogin(@RequestBody LoginForm email) {

        System.out.println(email);

        return email;
//        if (email == null || password == null) {
//            return "登录失败";
//        }
//
//        if (!Validator.isEmail(email) || !Validator.isPassword(password)) {
//            return "登录格式错误";
//        }

//        return "";
    }

    @PostMapping("/register")
    public String postRegister(@RequestBody RegisterForm form) {
        return form.getEmail();
    }
}
