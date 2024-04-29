package com.whut.springbootshiro.service;

import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.form.InterestForm;
import com.whut.springbootshiro.query.InterestQuery;

/**
 * 兴趣圈子接口
 *
 * @author Lei
 * @since  2024-04-28 23:29
 */
public interface InterestService {

    Result addInterest(InterestForm interestForm);

    Result deleteInterest(int interestId);

    Result updateInterest(InterestForm interestForm);

    Result page(InterestQuery interestQuery);
}
