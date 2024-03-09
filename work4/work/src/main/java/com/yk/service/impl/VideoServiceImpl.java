package com.yk.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.PutObjectRequest;
import com.yk.mapper.VideoMapper;
import com.yk.pojo.Result;
import com.yk.pojo.Video;
import com.yk.service.VideoService;
import com.yk.utils.RedisCache;
import com.yk.utils.SecurityContextHolderUtils;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author 12080
 */
@Component
@Slf4j
public class VideoServiceImpl implements VideoService {

    private String bucketName="***************";

    private String path="********************";


    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SecurityContextHolderUtils contextHolderUtils;

    private List<Video> list;

    @Autowired
    private COSClient cosClient;

//    @Async("taskExecutor")
    @Transactional
    @Override
    public Result uploadVideo(MultipartFile video ,String title, String description) throws IOException {
        log.info("数据传入");
        //获取文件原始名称
        String videoOriginalFilename = video.getOriginalFilename();
        log.info("数据流");
        InputStream videoInputStream = video.getInputStream();
        String videoKey = getFileKey(videoOriginalFilename);
        log.info("开始上传文件");
        cosClient.putObject(new PutObjectRequest(bucketName,videoKey,videoInputStream,null));
        cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        log.info("数据桶设置完毕");
        String videoUrl = path+"/"+videoKey;
        //获取文件的类型
        String videoType = FileUtil.extName(videoOriginalFilename);
        //获取文件大小
        long videoSize = video.getSize();

        videoMapper.uploadVideo(videoType, videoSize, videoUrl, title, description, contextHolderUtils.getUserId());
        log.info("数据存入数据库");
        cosClient.shutdown();
        log.info("cosClient关闭");
        return Result.success();
    }

    private String getFileKey(String originalFileName) {
        String filePath = "video/";
        //1.获取后缀名 2.去除文件后缀 替换所有特殊字符
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileStr = StrUtil.removeSuffix(originalFileName, fileType).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "_");
        filePath +=  new DateTime().toString("yyyyMMddHHmmss") + "_" + fileStr + fileType;
        return filePath;
    }


    @Override
    public Result listPublishByUserId(Long userId, Integer pageNum, Integer pageSize) {
        List<Video> publishListByUserId = videoMapper.listPublishByUserId(userId, pageNum, pageSize);
        int num = videoMapper.listPublishByUserIdNonPage(userId).size();
        if (pageNum < 0 || pageSize > num || pageNum > num) {
            return Result.error("数据有误");
        }
        return Result.successWithTotal(publishListByUserId,num);
    }

    @Transactional
    @Override
    public Result updateVideoVisitCount(Integer id) {
        log.info("数据传入");
        Long userId = contextHolderUtils.getUserId();
        String key = "video:visitUser:" + userId + "video:" + id;
        String key2 = "video:visitCount:video:";
        String key3 = "videoId:"+id;
        Set<String> keys = redisTemplate.keys(key3);
        Double score = redisTemplate.opsForZSet().score(key2, id);
        log.info("数据取出");
        if (redisCache.getCacheObject(key) != null) {
            return Result.error("操作失败");
        }
        Video video = videoMapper.queryVideoById(id);
        if (keys != null && !keys.isEmpty()) {
            for (String key4 : keys) {
                if (keys != null && !keys.isEmpty()) {
                    String key1 = key4.trim();
                    Integer visitCount = redisCache.getCacheObject(key1);
                    if (visitCount > 0) {
                        visitCount = visitCount + 1;
                        redisCache.setCacheObject(key3, visitCount);
                        log.info("数据存入redis");
                    } else {
                        redisCache.setCacheObject(key3, video.getVisitCount() + 1);
                        log.info("数据存入redis");
                    }
                } else {
                    redisCache.setCacheObject(key3, video.getVisitCount() + 1);
                    log.info("数据存入redis");
                }
            }
        } else {
            Integer v = video.getVisitCount() + 1;
            redisCache.setCacheObject(key3, v);
            log.info("数据存入redis");
        }
        if (score == null) {
            Integer visitCount = video.getVisitCount();
            redisTemplate.opsForZSet().add(key2, video, visitCount+1);
            log.info("数据存入redis");
        } else {
            redisTemplate.opsForZSet().incrementScore(key2, video, score + 1);
            log.info("数据存入redis");
        }
        redisCache.setCacheObject(key, userId);
        log.info("数据存入redis");
        redisCache.expire(key, 600);
        log.info("定时任务结束");
        return Result.success();
    }

    @Override
    public Result listVideoByKeyword(String keyword, Integer pageNum, Integer pageSize) {
        Long userId = contextHolderUtils.getUserId();
        String key = "search:history:"+userId;
        int size = videoMapper.listVideoByKeywordNonPage(keyword).size();
        if (keyword == null) {
            return Result.successWithTotal(videoMapper.listVideo(pageNum, pageSize),size);
        } else {
            redisCache.setCacheObject(key,keyword);
            return Result.successWithTotal(videoMapper.listVideoByKeyword(keyword, pageNum, pageSize),size);
        }
    }

    @Transactional
    @Override
    public Result deleteOwnVideoByVideoId(Integer id) {
        Long userId = contextHolderUtils.getUserId();
        boolean flag = videoMapper.deleteOwnVideoByVideoId(id,userId);
        if (flag){
            return Result.success();
        }else {
            return Result.error("稿件已删除或不存在");
        }
    }

    @Transactional
    @Override
    public Result banVideoByVideoId(Integer id) {
        boolean flag = videoMapper.banVideoByVideoId(id);
        if (flag){
            return Result.success();
        }else {
            return Result.error("稿件已被封禁或不存在");
        }
    }

    @Override
    public Result listPopular() {
        String key2 = "video:visitCount:video:";
        return Result.successWithTotal(redisTemplate.opsForZSet().reverseRange(key2, 0L, 10),redisTemplate.opsForZSet().reverseRange(key2, 0L, 10).size());
    }
}
