package com.yk.conf;

import com.yk.mapper.VideoMapper;
import com.yk.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 定时任务类
 * @author 12080
 */
@Component
@Slf4j
public class ScheduledConf {


    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发布定时增加视频浏览量任务
     */
    @Scheduled(cron = " 10 * * * * ?   ")
    public void updateVisitCount() {
        log.info("定时任务开启");
        Set keys = redisTemplate.keys("videoId:".concat("*"));
        List<Integer> list = new ArrayList<>();
        for (Object key : keys) {
            if (!key.toString().isEmpty()&& key != null) {
                String key1 = key.toString().trim();
                String[] split = key1.split(":");
                Integer id = Integer.parseInt(split[1]);
                list.add(id);
            }
        }
        for (Integer id : list) {
            Integer visitCount = redisCache.getCacheObject("videoId:"+id);
            videoMapper.updateVideoVisitCountByTime(id, visitCount);
            redisCache.deleteObject("videoId:"+id);
        }
    }
}