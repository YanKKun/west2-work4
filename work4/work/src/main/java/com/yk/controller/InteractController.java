package com.yk.controller;

import com.yk.pojo.Result;
import com.yk.pojo.Video;
import com.yk.service.InteractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 互动接口
 * @author 12080
 */
@RestController
public class InteractController {


    @Autowired
    private InteractService interactService;


    /**
     * 点赞指定视频
     *
     * @param videoId 视频id
     * @param action  1为点赞 2为取消点赞
     */
    @PostMapping("/like/video")
    @PreAuthorize("hasAnyAuthority('likeVideo')")
    public Result videoLike(@RequestParam(value = "video_id") Integer videoId,
                            @RequestParam(value = "action") Integer action) {
        if (action == 1) {
            return interactService.videoLike(videoId);
        } else if (action == 2) {
            return interactService.videoLike(videoId);
        }
        return Result.error("操作失败");
    }

    /**
     * 点赞指定评论
     * @param commentId 评论id
     * @param action 1为点赞 2为取消点赞
     */
    @PostMapping("/like/comment")
    @PreAuthorize("hasAnyAuthority('likeComment')")
    public Result commentLike(@RequestParam(value = "comment_id")Integer commentId,
                              @RequestParam(value = "action") Integer action) {
        if (action == 1) {
            return interactService.commentLike(commentId);
        } else if (action == 2) {
            return interactService.commentLike(commentId);
        }
        return Result.error("操作失败");
    }

    /**
     * 获取指定用户点赞的视频
     * @param userId 用户id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     */
    @GetMapping("/likeList/video")
    @PreAuthorize("hasAnyAuthority('likeVideoList')")
    public Result listVideoByUserLike(@RequestParam(value = "user_id")Long userId,
                                      @RequestParam(value = "page_num") Integer pageNum,
                                      @RequestParam(value = "page_size") Integer pageSize) {
       return interactService.listVideoByUserLike(userId, pageNum, pageSize);
    }

    /**
     * 发布评论
     * @param videoId 视频id
     * @param commentId 评论id
     * @param context 评论内容
     */
    @PostMapping("comment/publish")
    @PreAuthorize("hasAnyAuthority('commentPublish')")
    public Result publishComment(@RequestParam(value = "video_id")Integer videoId,
                                 @RequestParam(value = "comment_id") Integer commentId,
                                 @RequestParam(value = "context") String context) {
        if (commentId == 0) {
            return  interactService.saveVideoComment(videoId, context);
        } else if (videoId == 0) {
            return interactService.saveChildComment(context, commentId);
        } else {
            return Result.error("操作有误，请重新输入");
        }
    }

    /**
     * 删除用户自身发布的评论
     * @param videoId 视频id
     * @param commentId 评论id
     */
    @PostMapping("comment/delete")
    @PreAuthorize("hasAnyAuthority('commentDelete')")
    public Result deleteComment(@RequestParam(value = "video_id")Integer videoId,
                                @RequestParam(value = "comment_id") Integer commentId) {
        return interactService.deleteComment(videoId, commentId);
    }

    /**
     * 列出指定视频或评论的评论/子评论
     * @param videoId 视频id
     * @param commentId 评论id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     */
    @GetMapping("/comment/list")
    @PreAuthorize("hasAnyAuthority('listComment')")
    public Result listComment(@RequestParam(value = "video_id")Integer videoId,
                              @RequestParam(value = "comment_id") Integer commentId,
                              @RequestParam(value = "page_num") Integer pageNum,
                              @RequestParam(value = "page_size") Integer pageSize) {
        if (videoId==0){
            return interactService.listAllChildComment(commentId,pageNum,pageSize);
        }else if (commentId==0){
            return interactService.listAllVideoComment(videoId, pageNum, pageSize);
        }
        return Result.error("操作有误，请重新输入");
    }

    /**
     * 封禁指定用户在指定视频下发布的评论
     * @param videoId 视频id
     * @param userId 用户id
     * @param commentId 视频id
     */
    @PostMapping("/comment/ban")
    @PreAuthorize("hasAnyAuthority('commentBan')")
    public Result banComment(@RequestParam(value = "video_id")Integer videoId,
                             @RequestParam(value = "user_id")Long userId,
                             @RequestParam(value = "comment_id")Integer commentId){
        return interactService.banComment(videoId, userId, commentId);
    }


}
