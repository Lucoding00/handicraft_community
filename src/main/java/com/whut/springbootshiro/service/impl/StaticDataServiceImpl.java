package com.whut.springbootshiro.service.impl;

import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.config.DataInitConfig;
import com.whut.springbootshiro.form.StaticHot;
import com.whut.springbootshiro.form.StaticNew;
import com.whut.springbootshiro.mapper.PostMapper;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.service.StaticDataService;
import com.whut.springbootshiro.vo.PostVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 静态接口实现层
 *
 * @author Lei
 * @since 2024-05-08 23:48
 */
@Service
public class StaticDataServiceImpl implements StaticDataService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private DataInitConfig dataInitConfig;

    @Override
    public Result recommendation() {
        List<DataInitConfig.PredictModel> recommendationData = dataInitConfig.getRecommendationData();
        return new Result(recommendationData);
    }


    @Override
    public Object newPosts(StaticNew form) {
        List<PostVo> newLists = postMapper.selectNewList(form);
        HashMap<String, Object> data = new HashMap<>();
        data.put("new",newLists);
        return new Result(data);
    }

    @Override
    public Object hotPosts(StaticHot form) {
        List<PostVo> newLists = postMapper.selectHotList(form);
        HashMap<String, Object> data = new HashMap<>();
        data.put("hot",newLists);
        return new Result(data);
    }
}
