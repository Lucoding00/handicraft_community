package com.whut.springbootshiro.controller;

import com.whut.springbootshiro.form.InterestForm;
import com.whut.springbootshiro.form.UserAdminForm;
import com.whut.springbootshiro.form.UserInfoForm;
import com.whut.springbootshiro.query.InterestQuery;
import com.whut.springbootshiro.query.UserAdminQuery;
import com.whut.springbootshiro.service.InterestService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 兴趣圈子管理
 *
 * @author Lei
 * @since 2024-04-28 23:26
 */
@RestController
@RequestMapping("/interest")
public class InterestController {

    @Resource
    private InterestService interestService;

    /**
     * 添加兴趣圈子
     *
     * @param interestForm 兴趣圈子
     * @return 返回值
     */
    @PostMapping("addInterest")
    public Object addInterest(InterestForm interestForm) {
        return interestService.addInterest(interestForm);
    }

    /**
     * 删除兴趣圈子
     *
     * @param interestId 兴趣圈子id
     * @return 返回值
     */
    @PostMapping("deleteInterest")
    public Object deleteInterest(int interestId) {
        return interestService.deleteInterest(interestId);
    }


    /**
     * 更新兴趣圈子信息
     *
     * @param interestForm 兴趣圈子
     * @return 返回值
     */
    @PostMapping("updateInterest")
    public Object updateInterest(InterestForm interestForm) {
        return interestService.updateInterest(interestForm);
    }

    /**
     * 兴趣圈子分页
     *
     * @param interestQuery 兴趣圈子查询
     * @return 返回当前用户的兴趣圈子
     */
    @PostMapping("page")
    public Object page(InterestQuery interestQuery) {
        return interestService.page(interestQuery);
    }


}
