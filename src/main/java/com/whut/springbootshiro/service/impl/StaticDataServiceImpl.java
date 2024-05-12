package com.whut.springbootshiro.service.impl;

import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.form.StaticNewAndHotForm;
import com.whut.springbootshiro.mapper.PostMapper;
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

    @Override
    public Result newAndHot(StaticNewAndHotForm form) {
        List<PostVo> newLists = postMapper.selectNewList(form.getNewNum());
        List<PostVo> hotLists = postMapper.selectHotList(form.getHotNum());
        HashMap<String, Object> data = new HashMap<>();
        data.put("new",newLists);
        data.put("hot",hotLists);
        return new Result(data);
    }
}