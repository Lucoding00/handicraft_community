package com.whut.springbootshiro.config;

import com.whut.springbootshiro.eunm.PostOperationEnum;
import com.whut.springbootshiro.eunm.PostStatusEnum;
import com.whut.springbootshiro.mapper.PostMapper;
import com.whut.springbootshiro.mapper.PostOperationNumMapper;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.model.PostOperationNum;
import com.whut.springbootshiro.model.User;
import com.whut.springbootshiro.query.ReviewQuery;
import com.whut.springbootshiro.vo.PostVo;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Lei
 * @date 2024-05-18 16:16
 */
@Component
public class DataInitConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Resource(name = "staticData")
    private Map<String, Object> staticData;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PostMapper postMapper;

    @Resource
    private PostOperationNumMapper postOperationNumMapper;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        buildCacheData();
    }

    /**
     * 构建协同过滤的缓存数据
     */
    public void buildCacheData() {
        // 获取到用户记录
        List<User> user = userMapper.selectListByRole("user");

        ReviewQuery reviewQuery = new ReviewQuery();
        reviewQuery.setStatus(PostStatusEnum.AGREE.getValue());
        // 帖子列表的记录
        List<PostVo> postVos = postMapper.selectList(reviewQuery);
        // 用户id
        int[] userIds = user.stream().mapToInt(User::getId).toArray();
        Map<Integer, Integer> userIdIndexMap = new HashMap<>();
        for (int i = 0; i < userIds.length; i++) {
            userIdIndexMap.put(userIds[i], i);
        }
        // 帖子id
        int[] postIds = postVos.stream().mapToInt(PostVo::getId).toArray();
        Map<Integer, Integer> postIdIndexMap = new HashMap<>();
        for (int i = 0; i < postIds.length; i++) {
            postIdIndexMap.put(postIds[i], i);
        }
        List<PostOperationNum> post = postOperationNumMapper.selectList();
        Map<Integer, Map<Integer, List<PostOperationNum>>> collectList = post.stream().collect(Collectors.groupingBy(PostOperationNum::getUserId, Collectors.groupingBy(PostOperationNum::getPostId)));
        int[][] score = new int[userIds.length][postIds.length];

        Set<Integer> setUserIds = collectList.keySet();
        for (Integer userId : setUserIds) {
            Integer userIdArrIndex = userIdIndexMap.get(userId);
            Map<Integer, List<PostOperationNum>> userPostListMap = collectList.get(userId);
            Set<Integer> postSetKeys = userPostListMap.keySet();
            for (Integer postKey : postSetKeys) {
                Integer postIdArrIndex = postIdIndexMap.get(postKey);
                List<PostOperationNum> postOperationNums = userPostListMap.get(postKey);
                if (postOperationNums.isEmpty()) {
                    score[userIdArrIndex][postIdArrIndex] = 0;
                } else {
                    int curScore = 0;
                    for (PostOperationNum postOperationNum : postOperationNums) {
                        if (PostOperationEnum.LOOK.getValue().equals(postOperationNum.getOperationType())) {
                            curScore += 1;
                        } else if (PostOperationEnum.SHARE.getValue().equals(postOperationNum.getOperationType())
                                || PostOperationEnum.COLLECT.getValue().equals(postOperationNum.getOperationType())) {
                            curScore += 3;
                        } else if (PostOperationEnum.COIN.getValue().equals(postOperationNum.getOperationType())) {
                            curScore += 5;
                        } else {
                            curScore += 0;
                        }
                    }
                    score[userIdArrIndex][postIdArrIndex] = curScore;
                }
            }
        }
        staticData.put("userIds", userIds);
        staticData.put("postIds", postIds);
        staticData.put("score", score);
    }
}
