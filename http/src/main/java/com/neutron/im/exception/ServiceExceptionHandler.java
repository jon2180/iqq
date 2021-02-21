package com.neutron.im.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler {
    /**
     * 处理token异常
     */
    @ResponseBody
//    @ExceptionHandler({SignatureVerificationException.class, AlgorithmMismatchException.class, JWTDecodeException.class})
    @ExceptionHandler({RuntimeException.class})
    public String tokenErrorException() {
//        Result<String> result = new Result<>();
//        result.setCode(TOKEN_ERROR_EXCEPTION);
//        result.setMsg("无效的token！");
        log.error("无效的token");
        return "程序出错";
    }
}
