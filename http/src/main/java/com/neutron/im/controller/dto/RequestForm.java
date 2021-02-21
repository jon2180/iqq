package com.neutron.im.controller.dto;

import lombok.Data;
import lombok.ToString;

public class RequestForm {
    @Data
    @ToString
    public static class LoginForm {
        private String email;
        private String password;
    }

    @Data
    @ToString
    public static class RegisterForm {
        private String email;
        private String password;
    }
}
