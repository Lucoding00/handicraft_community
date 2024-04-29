package com.whut.springbootshiro.eunm;

/**
 * @author Lei
 * @date 2024-04-21 10:50
 */
public enum UserRoleEnum {

    /**
     * 管理员用户
     */
    ADMIN("admin"),

    /**
     * 用户
     */
    USER("user");

    private String value;

    UserRoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
