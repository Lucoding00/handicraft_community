package com.whut.springbootshiro.service;

import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.form.StaticNewAndHotForm;

/**
 * 静态数据接口
 *
 * @author Lei
 * @since  2024-05-08 23:48
 */
public interface StaticDataService {
    Result newAndHot(StaticNewAndHotForm form);
}
