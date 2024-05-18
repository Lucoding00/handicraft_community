package com.whut.springbootshiro;

import com.whut.springbootshiro.config.DataInitConfig;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;

@SpringBootTest
class DataInitConfigTest {

    @Resource
    private DataInitConfig dataInitConfig;


    @Test
    void testCalculate() {
        int[][] score = {
                {5, 3, 4, 4, -1},
                {3, 1, 2, 3, 3},
                {4, 3, 4, 3, 5},
                {3, 3, 1, 5, 4},
                {1, 5, 5, 2, 1},
        };
        DataInitConfig.PredictModel predictModel = dataInitConfig.getPredictModel(0, 4, score);
        System.out.println(predictModel);
        Assert.assertEquals(predictModel.getPrediction() + "", "7.8582679628027865");
    }

}
