package com.whut.springbootshiro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.form.InterestForm;
import com.whut.springbootshiro.mapper.InterestMapper;
import com.whut.springbootshiro.model.Interest;
import com.whut.springbootshiro.model.User;
import com.whut.springbootshiro.query.InterestQuery;
import com.whut.springbootshiro.service.InterestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Lei
 * @date 2024-04-28 23:29
 */
@Service
public class InterestServiceImpl implements InterestService {

    @Resource
    private InterestMapper interestMapper;


    @Override
    public Result addInterest(InterestForm interestForm) {
        Interest interest = new Interest();
        interest.setCreateTime(new Date());
        interest.setUpdateTime(new Date());
        BeanUtil.copyProperties(interestForm,interest);
        int result = interestMapper.insertSelective(interest);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result deleteInterest(int interestId) {
        int result = interestMapper.deleteByPrimaryKey(interestId);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result updateInterest(InterestForm interestForm) {
        Interest interest = interestMapper.selectByPrimaryKey(interestForm.getId());
        BeanUtil.copyProperties(interestForm,interest);
        int result = interestMapper.updateByPrimaryKeySelective(interest);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result page(InterestQuery interestQuery) {
        Page<Interest> interestPage = PageHelper.startPage(interestQuery.getPage(), interestQuery.getLimit());
        interestMapper.selectList(interestQuery);
        return new Result(interestPage.toPageInfo());
    }
}
