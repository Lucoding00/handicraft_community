package com.whut.springbootshiro.controller;

import com.whut.springbootshiro.form.StaticNewAndHotForm;
import com.whut.springbootshiro.service.StaticDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 静态数据的接口
 *
 * @author Lei
 * @since  2024-05-08 23:44
 */
@RestController
@RequestMapping("/static")
public class StaticDataController {

    @Resource
    private StaticDataService staticDataService;

    /**
     * 获取到当前最新和最热的帖子
     *
     * @param form 数据最新和最热的表单
     * @return 结构体数据
     */
    @PostMapping("newAndHot")
    public Object newAndHot(StaticNewAndHotForm form) {
        return staticDataService.newAndHot(form);
    }
}
