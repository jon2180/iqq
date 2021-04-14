package com.neutron.im.core.exception;

public class DisabledAccountException extends RuntimeException {
    public DisabledAccountException(String message) {
        super(message);
    }
}
