package com.yk.controller;


import com.yk.pojo.Result;
import com.yk.pojo.Video;
import com.yk.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 视频功能接口
 * @author 12080

 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 投稿
     *
     * @param video       视频文件
     * @param title       标题内容
     * @param description 简介
     */
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('upload')")
    public Result uploadVideo(@RequestParam MultipartFile video,
                              @RequestParam String title,
                              @RequestParam String description) throws IOException {
        return videoService.uploadVideo( video,title, description);
    }


    /**
     * 获取指定用户发布的稿件
     *
     * @param userId   用户id
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     */
    @GetMapping("/getPublish")
    @PreAuthorize("hasAnyAuthority('getPublish')")
    public Result listPublishByUserId(@RequestParam(value = "user_id")Long userId,
                                      @RequestParam(value = "page_num") Integer pageNum,
                                      @RequestParam(value = "page_size") Integer pageSize) {
        return videoService.listPublishByUserId(userId,pageNum,pageSize);
    }

    /**
     * 观看视频
     * @param id 视频id
     */
    @PostMapping("/visitVideo")
    @PreAuthorize("hasAnyAuthority('visitVideo')")
    public Result visitVideo(@RequestParam(value = "video_id")Integer id) {
       return videoService.updateVideoVisitCount(id);
    }

    /**
     * 根据关键字搜索视频，可分页查询
     * @param keyword 关键字
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('searchVideo')")
    public Result searchVideo(@RequestParam(value = "keyword")String keyword,
                              @RequestParam(value = "page_num") Integer pageNum,
                              @RequestParam(value = "page_size") Integer pageSize) {
        return videoService.listVideoByKeyword(keyword,pageNum,pageSize);
    }


    /**
     * 获取热门排行榜
     */
    @GetMapping("/popular")
    @PreAuthorize("hasAnyAuthority('popular')")
    public Result getPopular() {
        return videoService.listPopular();
    }


    /**
     * 删除发布的稿件
     * @param id 视频id
     */
    @PostMapping("/deleteVideo")
    @PreAuthorize("hasAnyAuthority('deleteOwnVideo')")
    public Result deleteOwnVideo(@RequestParam(value = "video_id")Integer id){
        return videoService.deleteOwnVideoByVideoId(id);
    }

    /**
     * 封禁违规稿件
     * @param id 视频id
     */
    @PostMapping("/banVideo")
    @PreAuthorize("hasAnyAuthority('banVideo')")
    public Result bankVideoByVideoId(@RequestParam(value = "video_id")Integer id){
        return videoService.banVideoByVideoId(id);
    }


}
