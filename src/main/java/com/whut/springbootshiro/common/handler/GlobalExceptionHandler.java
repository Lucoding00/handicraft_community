package com.whut.springbootshiro.common.handler;

import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.common.exception.BussiException;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 全局异常处理器
 * @author: Todd Ding
 * @date 2020-11-30 12:00
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    public Object handlerException(Exception exception) {
        exception.printStackTrace();
        // 判断是否是程序员自己定义的异常信息
        if (exception instanceof BussiException) {
            BussiException bussiException = (BussiException) exception;
            return new Result(bussiException);
        }
        // shiro
        if (exception instanceof ShiroException) {
            if (exception instanceof UnknownAccountException) {
                return new Result(CodeMsg.USER_USER_PASSWORD_ERROR);
            }
            if (exception instanceof AuthenticationException) {
                return new Result(CodeMsg.USER_NOT_HAVE_PERMISSION_ERROR);
            }
        }
        // 可能发生是其他异常
        return new Result(CodeMsg.ERROR);
    }
}