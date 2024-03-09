package com.yk.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.yk.mapper.InteractMapper;
import com.yk.pojo.Comment;
import com.yk.pojo.Result;
import com.yk.pojo.Video;
import com.yk.service.InteractService;
import com.yk.utils.RedisCache;
import com.yk.utils.SecurityContextHolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 互动业务实现类
 * @author 12080
 */
@Service
@Slf4j
public class InteractServiceImpl implements InteractService {

    @Autowired
    private InteractMapper interactMapper;

    @Autowired
    private SecurityContextHolderUtils securityContextHolderUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Result videoLike(Integer videoId) {
        Long userId = securityContextHolderUtils.getUserId();
        log.info("数据传入");
        String key = "video:liked:" + videoId;
        Boolean member = redisTemplate.opsForSet().isMember(key, userId.toString());
        if (BooleanUtil.isFalse(member)) {
            boolean flag = interactMapper.saveVideoLike(videoId);
            if (flag) {
                redisTemplate.opsForSet().add(key, userId.toString());
                interactMapper.saveVideoLikeOnMid(userId, videoId);
                log.info("数据存入");
                return Result.success();
            }
        } else {
            boolean flag = interactMapper.deleteVideoLike(videoId);
            if (flag) {
                redisTemplate.opsForSet().remove(key, userId.toString());
                interactMapper.deleteVideoLikeOnMid(userId, videoId);
                log.info("数据删除");
                return Result.success();
            }
        }
        return Result.error("操作有误");
    }


    @Transactional
    @Override
    public Result commentLike(Integer commentId) {
        Long userId = securityContextHolderUtils.getUserId();
        log.info("数据传入");
        String key = "comment:liked:" + commentId;
        Boolean member = redisTemplate.opsForSet().isMember(key, userId.toString());
        if (BooleanUtil.isFalse(member)) {
            boolean flag = interactMapper.saveCommentLike(commentId);
            if (flag) {
                redisTemplate.opsForSet().add(key, userId.toString());
                interactMapper.saveCommentLikeOnMid(userId, commentId);
                log.info("数据存入");
                return Result.success();
            }
        } else {
            boolean flag = interactMapper.deleteCommentLike(commentId);
            if (flag) {
                redisTemplate.opsForSet().remove(key, userId.toString());
                interactMapper.deleteCommentLikeOnMid(userId, commentId);
                log.info("数据删除");
                return Result.success();
            }
        }
        return Result.error("操作有误");
    }

    @Override
    public Result listVideoByUserLike(Long userId, Integer pageNum, Integer pageSize) {
        log.info("数据传入");
        List<Video> videos = interactMapper.listVideoByUserLike(userId, pageNum, pageSize);
        log.info("数据取出");
        if (pageNum < 0 || pageSize > videos.size()) {
            return Result.error("操作有误");
        }
        return Result.successWithTotal(videos,interactMapper.listAllVideoByUserLike(userId).size());
    }

    @Transactional
    @Override
    public Result saveVideoComment(Integer videoId, String context) {
        log.info("数据传入");
        Long userId = securityContextHolderUtils.getUserId();
        interactMapper.saveVideoComment(videoId, context, userId);
        log.info("数据存入数据库");
        interactMapper.saveVideoCommentCount(videoId);
        log.info("数据存入数据库");
        return Result.success();
    }

    @Transactional
    @Override
    public Result deleteComment(Integer videoId, Integer commentId) {
        Long userId = securityContextHolderUtils.getUserId();
        log.info("数据传入");
        Comment comment = interactMapper.findCommentById(commentId);
        boolean flag = interactMapper.deleteComment(videoId, userId, commentId);
        if (comment.getParentId() == 0&& flag) {
            interactMapper.updateVideoCommentCount(videoId);
            log.info("数据存入数据库");
            return Result.success();
        } else if(comment.getParentId() != 0&& flag){
            interactMapper.updateVideoCommentCount(videoId);
            log.info("数据存入数据库");
            interactMapper.updateChildCommentCount(comment.getParentId());
            log.info("数据存入数据库");
            return Result.success();
        }
        return Result.error("操作有误");
    }

    @Transactional
    @Override
    public Result saveChildComment(String context, Integer toCommentId) {
        Long userId = securityContextHolderUtils.getUserId();
        log.info("数据传入");
        Comment comment = interactMapper.findCommentById(toCommentId);
        interactMapper.saveChildComment(comment.getVideoId(), userId, toCommentId, context);
        log.info("数据存入数据库");
        interactMapper.saveChildCommentCount(toCommentId);
        log.info("数据存入数据库");
        interactMapper.saveVideoCommentCount(comment.getVideoId());
        log.info("数据存入数据库");
        return Result.success();
    }

    @Override
    public Result listAllVideoComment(Integer videoId, Integer pageNum, Integer pageSize) {
        log.info("数据传入");
        return Result.successWithTotal(interactMapper.listAllVideoComment(videoId, pageNum, pageSize),interactMapper.listAllVideoCommentNoPage(videoId).size());
    }

    @Override
    public Result listAllChildComment(Integer parentId, Integer pageNum, Integer pageSize) {
        log.info("数据传入");
        return Result.successWithTotal(interactMapper.listAllChildComment(parentId, pageNum, pageSize),interactMapper.listAllChildCommentNoPage(parentId).size());
    }

    @Transactional
    @Override
    public Result banComment(Integer videoId, Long userId, Integer commentId) {
        log.info("数据传入");
        interactMapper.deleteComment(videoId,userId,commentId);
        log.info("数据移除数据库");
        return Result.success();
    }
}
