package com.whut.springbootshiro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.eunm.PostOperationEnum;
import com.whut.springbootshiro.eunm.PostStatusEnum;
import com.whut.springbootshiro.form.PostForm;
import com.whut.springbootshiro.mapper.PostAttachmentMapper;
import com.whut.springbootshiro.mapper.PostMapper;
import com.whut.springbootshiro.mapper.PostOperationNumMapper;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.mapper.UserPostRelMapper;
import com.whut.springbootshiro.model.Post;
import com.whut.springbootshiro.model.PostOperationNum;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(PostForm postForm) {
        Post post = new Post();
        post.setCreateTime(new Date());
        post.setUpdateTime(new Date());
        post.setStatus(PostStatusEnum.PUBLISH.getValue());
        BeanUtil.copyProperties(postForm, post);
        int nextInt = postMapper.insertSelective(post);
        if (nextInt <= 0) {
            return new Result(CodeMsg.INSERT_POST_ERROR);
        }
        Integer id = post.getId();
        List<String> attachmentUrls = postForm.getAttachmentUrls();
        int res = postAttachmentMapper.insertBatch(id, attachmentUrls);
        if (res <= 0) {
            return new Result(CodeMsg.INSERT_POST_ERROR);
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
        PostOperationNum postOperationNum = postOperationNumMapper.selectByUserIdAndPostId(userId, postId);
        // 无点赞
        if (Objects.isNull(postOperationNum)) {
            PostOperationNum insertNum = new PostOperationNum();
            insertNum.setOperationType(PostOperationEnum.LIKE.getValue());
            insertNum.setUserId(userId);
            insertNum.setPostId(postId);
            postOperationNumMapper.insertSelective(insertNum);
            Integer likeNum = post.getLikeNum();
            post.setLikeNum((Objects.isNull(likeNum) ? 0 : likeNum) + 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        } else if (PostOperationEnum.LIKE.getValue().equals(postOperationNum.getOperationType())) { // 点赞了
            Integer likeNum = post.getLikeNum();
            post.setLikeNum((Objects.isNull(likeNum) ? 0 : likeNum) - 1);
            postMapper.updateByPrimaryKeySelective(post);
            postOperationNumMapper.deleteByPrimaryKey(postOperationNum.getId());
            return new Result(CodeMsg.SUCCESS);
        } else {
            postOperationNum.setOperationType(PostOperationEnum.LIKE.getValue());
            postOperationNumMapper.updateByPrimaryKeySelective(postOperationNum);
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
        PostOperationNum postOperationNum = postOperationNumMapper.selectByUserIdAndPostId(userId, postId);
        // 无点赞
        if (Objects.isNull(postOperationNum)) {
            PostOperationNum insertNum = new PostOperationNum();
            insertNum.setOperationType(PostOperationEnum.DISLIKE.getValue());
            insertNum.setUserId(userId);
            insertNum.setPostId(postId);
            postOperationNumMapper.insertSelective(insertNum);
            Integer disLikeNum = post.getDisLikeNum();
            post.setDisLikeNum((Objects.isNull(disLikeNum) ? 0 : disLikeNum) + 1);
            postMapper.updateByPrimaryKeySelective(post);
            return new Result(CodeMsg.SUCCESS);
        } else if (PostOperationEnum.DISLIKE.getValue().equals(postOperationNum.getOperationType())) { // 点赞了
            Integer disLikeNum = post.getDisLikeNum();
            post.setDisLikeNum((Objects.isNull(disLikeNum) ? 0 : disLikeNum) - 1);
            postMapper.updateByPrimaryKeySelective(post);
            postOperationNumMapper.deleteByPrimaryKey(postOperationNum.getId());
            return new Result(CodeMsg.SUCCESS);
        } else {
            postOperationNum.setOperationType(PostOperationEnum.DISLIKE.getValue());
            postOperationNumMapper.updateByPrimaryKeySelective(postOperationNum);
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
        // 无收藏
        if (Objects.isNull(postOperationNum)) {
            PostOperationNum insertNum = new PostOperationNum();
            insertNum.setOperationType(PostOperationEnum.COLLECT.getValue());
            insertNum.setUserId(userId);
            insertNum.setPostId(postId);
            postOperationNumMapper.insertSelective(insertNum);

            Integer coinNum = post.getCoinNum();
            post.setCoinNum((Objects.isNull(coinNum) ? 0 : coinNum) + 2);
            postMapper.updateByPrimaryKeySelective(post);
            userMapper.updateCoinNum(userId, currentUser.getCoinNum() - 2);
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
            postOperationNumMapper.insertSelective(insertNum);
            Integer shareNum = post.getShareNum();
            post.setShareNum((Objects.isNull(shareNum) ? 0 : shareNum) + 1);
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
}
