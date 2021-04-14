package com.neutron.im.core;

import lombok.Getter;
import lombok.Setter;

public class BasicException extends RuntimeException {

    @Setter
    @Getter
    private int statusCode;

    public BasicException(int code, String message) {
        super(message);
        this.statusCode = code;
    }
}
