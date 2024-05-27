package com.whut.springbootshiro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.eunm.PostOperationEnum;
import com.whut.springbootshiro.eunm.PostStatusEnum;
import com.whut.springbootshiro.form.PostForm;
import com.whut.springbootshiro.mapper.FollowerMapper;
import com.whut.springbootshiro.mapper.PostAttachmentMapper;
import com.whut.springbootshiro.mapper.PostMapper;
import com.whut.springbootshiro.mapper.PostOperationNumMapper;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.mapper.UserPostRelMapper;
import com.whut.springbootshiro.model.Follower;
import com.whut.springbootshiro.model.Post;
import com.whut.springbootshiro.model.PostOperationNum;
import com.whut.springbootshiro.model.PostState;
import com.whut.springbootshiro.model.User;
import com.whut.springbootshiro.model.UserPostRel;
import com.whut.springbootshiro.query.ReviewQuery;
import com.whut.springbootshiro.service.PostService;
import com.whut.springbootshiro.shiro.ActiveUser;
import com.whut.springbootshiro.util.AuthenticationUserUtil;
import com.whut.springbootshiro.vo.PostVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 发帖管理
 *
 * @author Lei
 * @since 2024-04-29 0:04
 */
@Service
public class PostServiceImpl implements PostService {
    @Resource
    private PostMapper postMapper;

    @Resource
    private PostAttachmentMapper postAttachmentMapper;

    @Resource
    private UserPostRelMapper userPostRelMapper;


    @Resource
    private PostOperationNumMapper postOperationNumMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FollowerMapper followerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(PostForm postForm) {
        Post post = new Post();
        Date date = new Date();
        post.setCreateTime(date);
        post.setUpdateTime(date);
        post.setStatus(PostStatusEnum.PUBLISH.getValue());
        BeanUtil.copyProperties(postForm, post);
        int nextInt = postMapper.insertSelective(post);
        if (nextInt <= 0) {
            return new Result(CodeMsg.INSERT_POST_ERROR);
        }
        Integer id = post.getId();
        List<String> attachmentUrls = postForm.getAttachmentUrls();
        if (!attachmentUrls.isEmpty()) {
            int res = postAttachmentMapper.insertBatch(id, attachmentUrls);
            if (res <= 0) {
                return new Result(CodeMsg.INSERT_POST_ERROR);
            }
        }
        Subject subject = SecurityUtils.getSubject();
        ActiveUser user = (ActiveUser) subject.getPrincipal();
        UserPostRel userPostRel = new UserPostRel();
        userPostRel.setUserId(user.getId());
        userPostRel.setPostId(id);
        int result = userPostRelMapper.insertSelective(userPostRel);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.INSERT_POST_ERROR);
        }
    }

    @Override
    public Result reviewPage(ReviewQuery reviewQuery) {
        Page<PostVo> interestPage = PageHelper.startPage(reviewQuery.getPage(), reviewQuery.getLimit());
        postMapper.selectList(reviewQuery);
        return new Result(interestPage.toPageInfo());
    }

    @Override
    public Result auditPost(int postId, PostStatusEnum postStatusEnum) {
        Post post = postMapper.selectByPrimaryKey(postId);
        if ((postStatusEnum.getValue().equals(PostStatusEnum.AGREE.getValue())
                || postStatusEnum.getValue().equals(PostStatusEnum.DISAGREE.getValue()))
                && !PostStatusEnum.PUBLISH.getValue().equals(postStatusEnum.getValue())) {
            return new Result(CodeMsg.PUBLISH_STATUS_ERROR);
        }
        post.setStatus(postStatusEnum.getValue());
        int result = postMapper.updateByPrimaryKeySelective(post);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result update(PostForm postForm) {
        Post post = new Post();
        BeanUtil.copyProperties(postForm, post);
        int nextInt = postMapper.updateByPrimaryKeySelective(post);
        if (nextInt <= 0) {
            return new Result(CodeMsg.UPDATE_POST_ERROR);
        }
        int res = postAttachmentMapper.deleteBatchByPostId(post.getId());
        if (res <= 0) {
            return new Result(CodeMsg.UPDATE_POST_ERROR);
        }
        res = postAttachmentMapper.insertBatch(post.getId(), postForm.getAttachmentUrls());
        if (res <= 0) {
            return new Result(CodeMsg.INSERT_POST_ERROR);
        }
        return new Result(CodeMsg.SUCCESS);
    }

    @Override
    public Result upPost(Integer postId) {
        Post post = postMapper.selectByPrimaryKey(postId);
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer userId = currentUser.getId();
        PostOperationNum postOperationNumUp = postOperationNumMapper.selectByUserIdAndPostIdAndStatus(userId, postId, PostOperationEnum.LIKE.getValue());
        PostOperationNum postOperationNumDown = postOperationNumMapper.selectByUserIdAndPostIdAndStatus(userId, postId, PostOperationEnum.DISLIKE.getValue());
        // 无点赞
        if (Objects.isNull(postOperationNumUp)
                && Objects.isNull(postOperationNumDown)) {
            PostOperationNum insertNum = new PostOperationNum();
            insertNum.setOperationType(PostOperationEnum.LIKE.getValue());
            insertNum.setUserId(userId);
            insertNum.setPostId(postId);
            insertNum.setCreateTime(new Date());
            postOperationNumMapper.insertSelective(insertNum);
            Integer likeNum = post.getLikeNum();
            post.setLikeNum((Objects.isNull(likeNum) ? 0 : likeNum) + 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        } else if (!Objects.isNull(postOperationNumUp)
                && Objects.isNull(postOperationNumDown)) { // 点赞了
            Integer likeNum = post.getLikeNum();
            post.setLikeNum((Objects.isNull(likeNum) ? 0 : likeNum) - 1);
            postMapper.updateByPrimaryKeySelective(post);
            postOperationNumMapper.deleteByPrimaryKey(postOperationNumUp.getId());
            return new Result(CodeMsg.SUCCESS);
        } else {
            postOperationNumDown.setOperationType(PostOperationEnum.LIKE.getValue());
            postOperationNumMapper.updateByPrimaryKeySelective(postOperationNumDown);
            Integer likeNum = post.getLikeNum();
            Integer dislikeNum = post.getDisLikeNum();
            post.setLikeNum((Objects.isNull(likeNum) ? 0 : likeNum) + 1);
            post.setDisLikeNum((Objects.isNull(dislikeNum) ? 0 : dislikeNum) - 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        }
    }

    @Override
    public Result downPost(Integer postId) {
        Post post = postMapper.selectByPrimaryKey(postId);
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer userId = currentUser.getId();
        PostOperationNum postOperationNumUp = postOperationNumMapper.selectByUserIdAndPostIdAndStatus(userId, postId, PostOperationEnum.LIKE.getValue());
        PostOperationNum postOperationNumDown = postOperationNumMapper.selectByUserIdAndPostIdAndStatus(userId, postId, PostOperationEnum.DISLIKE.getValue());

        // 无点赞
        if (Objects.isNull(postOperationNumUp)
                && Objects.isNull(postOperationNumDown)) {
            PostOperationNum insertNum = new PostOperationNum();
            insertNum.setOperationType(PostOperationEnum.DISLIKE.getValue());
            insertNum.setUserId(userId);
            insertNum.setPostId(postId);
            insertNum.setCreateTime(new Date());
            postOperationNumMapper.insertSelective(insertNum);
            Integer disLikeNum = post.getDisLikeNum();
            post.setDisLikeNum((Objects.isNull(disLikeNum) ? 0 : disLikeNum) + 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        } else if (!Objects.isNull(postOperationNumDown)
                && Objects.isNull(postOperationNumUp)) { // 点赞了
            Integer disLikeNum = post.getDisLikeNum();
            post.setDisLikeNum((Objects.isNull(disLikeNum) ? 0 : disLikeNum) - 1);
            postMapper.updateByPrimaryKeySelective(post);
            postOperationNumMapper.deleteByPrimaryKey(postOperationNumDown.getId());
            return new Result(CodeMsg.SUCCESS);
        } else {
            postOperationNumUp.setOperationType(PostOperationEnum.DISLIKE.getValue());
            postOperationNumMapper.updateByPrimaryKeySelective(postOperationNumUp);
            Integer likeNum = post.getLikeNum();
            Integer dislikeNum = post.getDisLikeNum();
            post.setLikeNum((Objects.isNull(likeNum) ? 0 : likeNum) - 1);
            post.setDisLikeNum((Objects.isNull(dislikeNum) ? 0 : dislikeNum) + 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        }
    }


    @Override
    public Result coinPost(Integer postId) {
        Post post = postMapper.selectByPrimaryKey(postId);
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        // 硬币数量不够投币了，请充值
        if (currentUser.getCoinNum() < 2) {
            return new Result(CodeMsg.COIN_NUM_IS_OK);
        }
        Integer userId = currentUser.getId();
        PostOperationNum postOperationNum = postOperationNumMapper.selectByUserIdAndPostIdAndStatus(userId, postId, PostOperationEnum.COIN.getValue());
        if (Objects.isNull(postOperationNum)) {
            PostOperationNum insertNum = new PostOperationNum();
            insertNum.setOperationType(PostOperationEnum.COIN.getValue());
            insertNum.setUserId(userId);
            insertNum.setPostId(postId);
            insertNum.setCreateTime(new Date());
            postOperationNumMapper.insertSelective(insertNum);
            Integer coinNum = post.getCoinNum();
            post.setCoinNum((Objects.isNull(coinNum) ? 0 : coinNum) + 2);
            postMapper.updateByPrimaryKeySelective(post);
            userMapper.updateCoinNum(userId, currentUser.getCoinNum() - 2);
            UserPostRel userPostRel = userPostRelMapper.selectByPostId(postId);
            Integer userId1 = userPostRel.getUserId();
            User user = userMapper.selectByPrimaryKey(userId1);
            user.setCoinNum(user.getCoinNum() + 2);
            userMapper.updateByPrimaryKeySelective(user);
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result collectPost(Integer postId) {
        Post post = postMapper.selectByPrimaryKey(postId);
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer userId = currentUser.getId();
        PostOperationNum postOperationNum = postOperationNumMapper.selectByUserIdAndPostIdAndStatus(userId, postId, PostOperationEnum.COLLECT.getValue());
        // 无收藏
        if (Objects.isNull(postOperationNum)) {
            PostOperationNum insertNum = new PostOperationNum();
            insertNum.setOperationType(PostOperationEnum.COLLECT.getValue());
            insertNum.setUserId(userId);
            insertNum.setPostId(postId);
            insertNum.setCreateTime(new Date());
            postOperationNumMapper.insertSelective(insertNum);
            Integer collectNum = post.getCollectNum();
            post.setCollectNum((Objects.isNull(collectNum) ? 0 : collectNum) + 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        } else {
            postOperationNumMapper.deleteByPrimaryKey(postOperationNum.getId());
            Integer collectNum = post.getCollectNum();
            post.setCollectNum((Objects.isNull(collectNum) ? 0 : collectNum) - 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        }
    }

    @Override
    public Result sharePost(Integer postId) {
        Post post = postMapper.selectByPrimaryKey(postId);
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer userId = currentUser.getId();
        PostOperationNum postOperationNum = postOperationNumMapper.selectByUserIdAndPostIdAndStatus(userId, postId, PostOperationEnum.SHARE.getValue());
        // 无收藏
        if (Objects.isNull(postOperationNum)) {
            PostOperationNum insertNum = new PostOperationNum();
            insertNum.setOperationType(PostOperationEnum.SHARE.getValue());
            insertNum.setUserId(userId);
            insertNum.setPostId(postId);
            insertNum.setCreateTime(new Date());
            postOperationNumMapper.insertSelective(insertNum);
            Integer shareNum = post.getShareNum();
            post.setShareNum((Objects.isNull(shareNum) ? 0 : shareNum) + 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        } else {
            postOperationNumMapper.deleteByPrimaryKey(postOperationNum.getId());
            Integer shareNum = post.getShareNum();
            post.setShareNum((Objects.isNull(shareNum) ? 0 : shareNum) - 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        }
    }

    @Override
    public Result lookPost(Integer postId) {
        Post post = postMapper.selectByPrimaryKey(postId);
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer userId = currentUser.getId();
        PostOperationNum postOperationNum = postOperationNumMapper.selectLastLook(userId, postId);
        if (!Objects.isNull(postOperationNum) && isWithin5Minutes(new Date(), postOperationNum.getCreateTime())) {
            return new Result(CodeMsg.LOOK_TIME_WITHIN_FIVE_TIME);
        } else {
            PostOperationNum insertNum = new PostOperationNum();
            insertNum.setOperationType(PostOperationEnum.LOOK.getValue());
            insertNum.setUserId(userId);
            insertNum.setPostId(postId);
            insertNum.setCreateTime(new Date());
            postOperationNumMapper.insertSelective(insertNum);
            Integer lookNum = post.getLookNum();
            post.setLookNum((Objects.isNull(lookNum) ? 0 : lookNum) + 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        }
    }

    private boolean isWithin5Minutes(Date currentTime, Date otherTime) {
        // 计算时间差（以毫秒为单位）
        long diff = currentTime.getTime() - otherTime.getTime();
        // 转换为分钟
        long diffMinutes = diff / (60 * 1000);

        // 判断时间差是否小于5分钟
        return Math.abs(diffMinutes) < 5;
    }


    @Override
    public Result getPost(Integer postId) {
        Post post = postMapper.selectByPrimaryKey(postId);
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        PostState postState = new PostState();
        UserPostRel userPostRel = userPostRelMapper.selectByPostId(postId);
        User user = userMapper.selectByPrimaryKey(userPostRel.getUserId());
        postState.setCreateUser(user);
        BeanUtil.copyProperties(post, postState);
        Follower follower = followerMapper.selectByPostIdAndFollower(currentUserId, user.getId());
        if (!Objects.isNull(follower)) {
            postState.setFollow(true);
        }
        List<PostOperationNum> postOperationNums = postOperationNumMapper.selectByUserIdAndPostId(currentUserId, postId);
        List<String> collectString = postOperationNums.stream().map(PostOperationNum::getOperationType).collect(Collectors.toList());
        if (collectString.contains(PostOperationEnum.LIKE.getValue())) {
            postState.setLike(true);
        } else if (collectString.contains(PostOperationEnum.DISLIKE.getValue())) {
            postState.setDisLike(true);
        } else if (collectString.contains(PostOperationEnum.SHARE.getValue())) {
            postState.setShare(true);
        } else if (collectString.contains(PostOperationEnum.COIN.getValue())) {
            postState.setCoin(true);
        } else if (collectString.contains(PostOperationEnum.COLLECT.getValue())) {
            postState.setCollect(true);
        }

        return new Result(postState);
    }

    @Override
    public Result attention(Integer postUserId) {
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        Follower follower = followerMapper.selectByPostIdAndFollower(currentUserId, postUserId);
        if (!Objects.isNull(follower)) {
            followerMapper.deleteByPrimaryKey(follower.getId());// 取消关注
        } else {
            // 关注
            Follower follower1 = new Follower();
            follower1.setFans(currentUserId);
            follower1.setPoster(postUserId);
            followerMapper.insertSelective(follower1);
        }
        return new Result(CodeMsg.SUCCESS);
    }
}
