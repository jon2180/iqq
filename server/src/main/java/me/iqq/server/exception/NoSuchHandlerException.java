package me.iqq.server.exception;

public class NoSuchHandlerException extends RuntimeException {
    public NoSuchHandlerException(String msg) {
        super(msg);
    }
}
