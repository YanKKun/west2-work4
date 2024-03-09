package com.yk.service;

import com.yk.pojo.Comment;
import com.yk.pojo.Result;
import com.yk.pojo.Video;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 互动业务层
 * @author 12080
 */
@Service
public interface InteractService {

    /**
     * 点赞视频
     * @param videoId 视频id
     * @return 结果
     */
    Result videoLike(Integer videoId);

    /**
     * 点赞评论
     * @param commentId 评论id
     * @return 结果
     */
    Result commentLike(Integer commentId);


    /**
     * 获取指定用户点赞视频列表，可分页
     * @param userId 用户id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 结果
     */
    Result listVideoByUserLike(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 发布子评论
     * @param videoId 视频id
     * @param context 评论内容
     * @return 结果
     */
    Result saveVideoComment(Integer videoId, String context);

    /**
     * 删除指定评论
     * @param videoId 视频id
     * @param commentId 评论id
     * @return 结果
     */
    Result deleteComment(Integer videoId, Integer commentId);

    /**
     * 发布子评论
     * @param toCommentId 父评论id
     * @param context 评论内容
     * @return 结果
     */
    Result saveChildComment(String context, Integer toCommentId);

    /**
     * 返回指定视频的所有评论可分页
     * @param videoId 视频id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 结果
     */
    Result listAllVideoComment(Integer videoId, Integer pageNum, Integer pageSize);

    /**
     * 返回指定评论的所有子评论，可分页
     * @param parentId 父评论id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 结果
     */
    Result listAllChildComment(Integer parentId, Integer pageNum, Integer pageSize);

    /**
     * 封禁评论
     * @param videoId 视频id
     * @param userId 用户id
     * @param commentId 评论id
     * @return 结果
     */
    Result banComment(Integer videoId, Long userId, Integer commentId);
}
