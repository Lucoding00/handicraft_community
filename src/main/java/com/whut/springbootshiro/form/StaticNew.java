package com.whut.springbootshiro.form;

import lombok.Data;

/**
 * 获取到当前的最新和最热的表单
 *
 * @author Lei
 * @since 2024-05-09 0:13
 */
@Data
public class StaticNew {
    /**
     * 最新
     */
    private int newNum;

    /**
     * 分类
     */
    private String category;
}
