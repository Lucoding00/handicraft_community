package com.whut.springbootshiro.eunm;

/**
 * @author Lei
 * @date 2024-04-21 10:53
 */
public enum UserStatusEnum {
    /**
     * 可用
     */
    AVAILABLE("available"),

    /**
     * 注销
     */
    UNUSABLE("unusable");

    private String value;

    UserStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
