package com.whut.springbootshiro.form;

import lombok.Data;

/**
 * 兴趣表单
 *
 * @author Lei
 * @date 2024-04-28 23:28
 */
@Data
public class InterestForm {

    private Integer id;

    private String interestName;

    private String describe;

    private String cover;
}
