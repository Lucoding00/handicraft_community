package com.whut.springbootshiro.config;

import com.whut.springbootshiro.eunm.PostOperationEnum;
import com.whut.springbootshiro.eunm.PostStatusEnum;
import com.whut.springbootshiro.mapper.PostMapper;
import com.whut.springbootshiro.mapper.PostOperationNumMapper;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.model.PostOperationNum;
import com.whut.springbootshiro.model.User;
import com.whut.springbootshiro.query.ReviewQuery;
import com.whut.springbootshiro.shiro.ActiveUser;
import com.whut.springbootshiro.util.AuthenticationUserUtil;
import com.whut.springbootshiro.vo.PostVo;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
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

    private static final int FLAG_NUM = -1;

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
        staticData.put("userIdIndexMap", userIdIndexMap);
        staticData.put("postIds", postIds);
        staticData.put("postIdIndexMap", postIdIndexMap);
        staticData.put("score", score);
    }

    public List<PredictModel> getRecommendationData() {
        Map<Integer, Integer> userIdIndexMap = (Map<Integer, Integer>) staticData.get("userIdIndexMap");
        Map<Integer, Integer> postIdIndexMap = (Map<Integer, Integer>) staticData.get("postIdIndexMap");
        int[][] score = (int[][]) staticData.get("score");
        int[] userIds = (int[]) staticData.get("userIds");
        int[] postIds = (int[]) staticData.get("postIds");

        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        Integer targetUserId = userIdIndexMap.get(currentUserId);

        List<PredictModel> predictModels = new ArrayList<>();
        List<SimilarityModel> nearestNeighbors = findNearestNeighbors(targetUserId, score);
        for (int i = 0; i < postIds.length; i++) {
            if (score[targetUserId][i] != FLAG_NUM) {
                int itemValue = score[targetUserId][i];
                score[targetUserId][i] = FLAG_NUM;
                PredictModel predictModel = generateRecommendations(targetUserId, i, score, nearestNeighbors);
                predictModel.setUserId(userIds[targetUserId]);
                predictModel.setPostId(postIds[i]);
                score[targetUserId][i] = itemValue;
                predictModels.add(predictModel);
            }
        }

        List<PredictModel> collectData = predictModels
                .stream()
                .sorted(Comparator.comparing(PredictModel::getPrediction, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());

        List<Integer> predictPostIds = collectData.stream().map(PredictModel::getPostId).collect(Collectors.toList());
        List<PostVo> postList = postMapper.selectListByIds(predictPostIds);
        Map<Integer, PostVo> integerPostVoHashMap = new HashMap<>();
        for (int i = 0; i < postIdIndexMap.size(); i++) {
            PostVo postVo = postList.get(i);
            integerPostVoHashMap.put(postVo.getId(),postVo);
        }
        collectData.forEach(item->{
            item.setPostVo(integerPostVoHashMap.get(item.getPostId()));
        });
        return collectData;
    }


    /**
     * 协同过滤算法-计算目标用户对于目标帖子的评分  方便测试
     *
     * @param targetUserId 目标用户
     * @param targetPostId 目标帖子
     * @param score        评分举证
     * @return 返回
     */
    public PredictModel getPredictModel(int targetUserId, int targetPostId, int[][] score) {
        List<SimilarityModel> nearestNeighbors = findNearestNeighbors(targetUserId, score);
        return generateRecommendations(targetUserId, targetPostId, score, nearestNeighbors);
    }


    /**
     * 协同过滤算法-计算相似邻居
     *
     * @param targetUserIndex 需要推荐的用户下标
     * @param score           评分数组
     * @return 相似评分列表
     */
    private List<SimilarityModel> findNearestNeighbors(int targetUserIndex, int[][] score) {
        int[] targetUserRatings = score[targetUserIndex];
        List<SimilarityModel> similarityModels = new ArrayList<>();
        for (int i = 0; i < score.length; i++) {
            if (i != targetUserIndex) {
                double similarity = calculateUserSimilarity(targetUserRatings, score[i]);
                similarityModels.add(new SimilarityModel(i, similarity));
            }
        }
        return similarityModels
                .stream()
                .sorted(Comparator.comparing(SimilarityModel::getSimilarity, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * 协同过滤算法-计算预测评分
     *
     * @param targetUserIndex  推荐用户
     * @param targetPostIndex  推荐帖子
     * @param score            评分数组
     * @param nearestNeighbors 计算的邻居
     * @return 预测评分
     */
    private PredictModel generateRecommendations(int targetUserIndex
            , int targetPostIndex
            , int[][] score
            , List<SimilarityModel> nearestNeighbors) {
        int[] user1Ratings = score[targetUserIndex];
        int totalRate1 = 0;
        int totalNum1 = 0;
        double averageUser1;
        for (int user1Rating : user1Ratings) {
            if (user1Rating != FLAG_NUM) {
                totalNum1++;
                totalRate1 += user1Rating;
            }
        }
        averageUser1 = totalRate1 / (totalNum1 * 1.0);

        double total = 0;
        double totalSim = 0;
        for (SimilarityModel element : nearestNeighbors) {
            totalSim += element.similarity;

            int preUserId = element.userId;
            int userRatingTargetItem = score[preUserId][targetPostIndex];

            int[] user2Ratings = score[preUserId];
            int totalRate2 = 0;
            int totalNum2 = 0;
            double averageUser2;
            for (int user2Rating : user2Ratings) {
                if (user2Rating != FLAG_NUM) {
                    totalNum2++;
                    totalRate2 += user2Rating;
                }
            }
            averageUser2 = totalRate2 / (totalNum2 * 1.0);
            total += (element.similarity * ((userRatingTargetItem - averageUser2) * 1.0));
        }
        double prediction = averageUser1 + total / (totalSim * 1.0);
        return new PredictModel(targetUserIndex, targetPostIndex, prediction);
    }


    /**
     * 协同过滤算法-计算预测评分
     *
     * @param user1Ratings 用户1评分
     * @param user2Ratings 用户2频分
     * @return 计算相似度
     */
    private double calculateUserSimilarity(int[] user1Ratings, int[] user2Ratings) {
        double sumOfSquaresDiff = 0;
        double sumOfSquaresDiffSqrt1 = 0;
        double sumOfSquaresDiffSqrt2 = 0;
        int totalRate1 = 0;
        int totalNum1 = 0;
        double averageUser1;
        for (int user1Rating : user1Ratings) {
            if (user1Rating != FLAG_NUM) {
                totalNum1++;
                totalRate1 += user1Rating;
            }
        }
        averageUser1 = totalRate1 / (totalNum1 * 1.0);

        int totalRate2 = 0;
        int totalNum2 = 0;
        double averageUser2;
        for (int user2Rating : user2Ratings) {
            if (user2Rating != FLAG_NUM) {
                totalNum2++;
                totalRate2 += user2Rating;
            }
        }
        averageUser2 = totalRate2 / (totalNum2 * 1.0);


        for (int i = 0; i < user1Ratings.length; i++) {
            if (user1Ratings[i] != FLAG_NUM && user2Ratings[i] != FLAG_NUM) {
                sumOfSquaresDiff += (user1Ratings[i] - averageUser1) * (user2Ratings[i] - averageUser2);
            }
        }

        for (int i = 0; i < user1Ratings.length; i++) {
            if (user1Ratings[i] != FLAG_NUM && user2Ratings[i] != FLAG_NUM) {
                sumOfSquaresDiffSqrt1 += (user1Ratings[i] - averageUser1) * (user1Ratings[i] - averageUser1);
            }
        }
        for (int i = 0; i < user2Ratings.length; i++) {
            if (user1Ratings[i] != FLAG_NUM && user2Ratings[i] != FLAG_NUM) {
                sumOfSquaresDiffSqrt2 += (user2Ratings[i] - averageUser2) * (user2Ratings[i] - averageUser2);
            }
        }
        return sumOfSquaresDiff / (Math.sqrt(sumOfSquaresDiffSqrt1) * Math.sqrt(sumOfSquaresDiffSqrt2) * 1.0);
    }

    /**
     * 预测模型
     */
    public class PredictModel {

        private int userId;

        private int postId;

        private double prediction;

        private PostVo postVo;

        public PredictModel(int userId, int postId, double prediction) {
            this.userId = userId;
            this.postId = postId;
            this.prediction = prediction;
        }

        public int getUserId() {
            return userId;
        }

        public PostVo getPostVo() {
            return postVo;
        }

        public void setPostVo(PostVo postVo) {
            this.postVo = postVo;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getPostId() {
            return postId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }

        public double getPrediction() {
            return prediction;
        }

        public void setPrediction(double prediction) {
            this.prediction = prediction;
        }

        @Override
        public String toString() {
            return "PredictModel{" +
                    "userId=" + userId +
                    ", postId=" + postId +
                    ", prediction=" + prediction +
                    '}';
        }
    }

    /**
     * 相似模型
     */
    class SimilarityModel {

        private int userId;

        private double similarity;

        public SimilarityModel(int userId, double similarity) {
            this.userId = userId;
            this.similarity = similarity;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public double getSimilarity() {
            return similarity;
        }

        public void setSimilarity(double similarity) {
            this.similarity = similarity;
        }
    }
}
