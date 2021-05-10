package com.neutron.im.core;

import com.neutron.im.core.exception.AuthFailedException;
import com.neutron.im.core.exception.DisabledAccountException;
import com.neutron.im.core.exception.NoSuchAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
//@RestControllerAdvice
@ControllerAdvice
public class ServiceExceptionHandler {
    /**
     * 处理token异常
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultVO handleException(Exception e) {
        e.printStackTrace();
        return ResultVO.failed(StatusCode.S500_LOGIC_ERROR, e.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleNormalException(RuntimeException e) {

        e.printStackTrace();
        return "forward:/error";
    }

    @ExceptionHandler({IllegalArgumentException.class, AuthFailedException.class,
        DisabledAccountException.class, NoSuchAccountException.class,
        MissingServletRequestParameterException.class, MissingServletRequestPartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResultVO handle400Exception(RuntimeException e) {
        e.printStackTrace();
        return ResultVO.failed(StatusCode.S400_EMPTY_PARAMETER, e.getMessage());
    }
}
