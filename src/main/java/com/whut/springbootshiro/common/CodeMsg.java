package com.whut.springbootshiro.common;

public enum CodeMsg {

    SUCCESS(200, "ok"),
    ERROR(110, "error"),
    AUTH_ERROR(400, "no permissions"),
    CODE_ERROR(440, "The verification code is incorrect"),
    CODE_INVALID(441, "The verification code is invalid"),

    USER_USER_PASSWORD_ERROR(4001001, "Wrong username or password"),
    USER_NOT_HAVE_PERMISSION_ERROR(4001005, "Insufficient user permissions"),
    UPLOAD_IMG_ERROR(4001006, "File upload error"),
    STATUS_IS_NOT_OK(4001007, "The current status is not available"),
    INSERT_POST_ERROR(4001008, "The post insert occur a error"),
    UPDATE_POST_ERROR(4001009, "The post update occur a error"),
    UP_COMMENT_ERROR(4001010, "Not allowed to give up it to yourself ..."),
    UP_REPEAT_COMMENT_ERROR(4001011, "Not allowed to repeat give up it to yourself ..."),
    ;
    public Integer code; // 业务码
    public String msg; // 业务消息

    CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
