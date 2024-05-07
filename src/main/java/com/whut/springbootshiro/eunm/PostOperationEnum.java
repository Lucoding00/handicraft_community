package com.whut.springbootshiro.eunm;

/**
 * 帖子操作的枚举类
 *
 * @author Lei
 * @date 2024-05-08 0:17
 */
public enum PostOperationEnum {
    LIKE("like"),

    DISLIKE("dislike"),

    SHARE("share"),

    COIN("coin"),

    COLLECT("collect");

    private String value;

    PostOperationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
