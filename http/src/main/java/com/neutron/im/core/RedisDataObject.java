package com.neutron.im.core;

import lombok.Data;

@Data
public class RedisDataObject {
    private String captcha;
    private String emailCaptcha;
}
