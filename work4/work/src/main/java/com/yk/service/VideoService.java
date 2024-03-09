package com.yk.service;

import com.yk.pojo.Result;
import com.yk.pojo.Video;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 视频业务层
 * @author 12080
 */
@Service
public interface VideoService {

    /**
     * 投稿
     * @param video 视频文件
     * @param title 视频标题
     * @param description 视频简介
     * @throws IOException 文件输入异常签名
     * @return 结果
     */
    Result uploadVideo(MultipartFile video, String title, String description) throws IOException;

    /**
     * 获取用户投稿列表，可分页
     * @param userId 用户id
     * @param pageNum 页码
     * @param pageSize 页码大小
     * @return 结果
     */
    Result listPublishByUserId(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 增加视频点击量
     * @param id 视频id
     * @return 结果
     */
    Result updateVideoVisitCount(Integer id);

    /**
     * 通过关键字获取视频,可分页
     * @param keyword 关键字
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @return 结果
     */
    Result listVideoByKeyword(String keyword, Integer pageNum, Integer pageSize);



    /**
     * 删除登录用户的投稿
     * @param id  视频id
     * @return 结果
     */
    Result deleteOwnVideoByVideoId(Integer id);

    /**
     * 封禁指定视频
     * @param id 视频id
     * @return 结果
     */
    Result banVideoByVideoId(Integer id);

    /**
     * 获取热门排行榜
     * @return 结果
     */
    Result listPopular();
}
