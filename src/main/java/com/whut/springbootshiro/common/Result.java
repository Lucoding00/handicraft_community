package com.whut.springbootshiro.common;

import com.whut.springbootshiro.common.exception.BussiException;
import lombok.Data;

@Data
public class Result {
    /**
     * 业务码
     */
    private Integer code;
    /**
     * 业务消息
     */
    private String msg;
    /**
     * 业务数据
     */
    private Object data;

    /*
     * 业务就分为2种情况:
     *       1. 成功
     *           1. 成功了没有数据
     *           2. 成功了有返回数据
     *       2. 失败
     *           失败时没有数据 失败原因
     */

    /**
     * 成功没有数据
     */
    public Result() {
        this.code = CodeMsg.SUCCESS.code;
        this.msg = CodeMsg.SUCCESS.msg;
    }

    /**
     * 成功 有数据
     *
     * @param data
     */
    public Result(Object data) {
        this();
        this.data = data;
    }

    /**
     * 失败 没有数据
     * 但是 有错误码  和  错误消息
     *
     * @param codeMsg
     */
    public Result(CodeMsg codeMsg) {
        this.code = codeMsg.code;
        this.msg = codeMsg.msg;
    }

    /**
     * 兼容异常信息
     *
     * @param bussiException
     */
    public Result(BussiException bussiException) {
        this.code = bussiException.getCode();
        this.msg = bussiException.getMessage();
    }

}
