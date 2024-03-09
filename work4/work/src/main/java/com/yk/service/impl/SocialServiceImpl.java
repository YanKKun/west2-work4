package com.yk.service.impl;

import com.yk.dto.UserDTO;
import com.yk.mapper.SocialMapper;
import com.yk.mapper.UserMapper;
import com.yk.pojo.Result;
import com.yk.service.SocialService;
import com.yk.utils.SecurityContextHolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 社交接口实现类
 * @author 12080
 */
@Service
@Slf4j
public class SocialServiceImpl implements SocialService {


    @Autowired
    private SocialMapper socialMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SecurityContextHolderUtils securityContextHolderUtils;


    @Transactional
    @Override
    public Result saveFollowByUserId(Long followId) {
        Long userId = securityContextHolderUtils.getUserId();
        List<String> flag = socialMapper.findUserAndFollowById(userId, followId);
        log.info("数据传入");
        String key = "follows:" + userId + ":" + followId;
        if (!flag.isEmpty()) {
            return Result.error("已关注该用户或用户不存在");
        }
        redisTemplate.opsForSet().add(key, followId);
        log.info("数据存入redis");
        socialMapper.saveFollowByUserId(userId, followId);
        socialMapper.saveFollow(userId);
        socialMapper.saveFans(followId);
        log.info("数据存入数据库");
        return Result.success();
    }

    @Transactional
    @Override
    public Result deletedFollowByUserId(Long followId) {
        Long userId = securityContextHolderUtils.getUserId();
        List<String> flag = socialMapper.findUserAndFollowById(userId, followId);
        log.info("数据传入");
        String key = "follows:" + userId + ":" + followId;
        if (flag.isEmpty()) {
            return Result.error("未关注该用户或用户不存在");
        }
        redisTemplate.opsForSet().remove(key, followId);
        log.info("数据从redis中删除");
        socialMapper.deletedFollowByUserId(userId, followId);
        socialMapper.deleteFans(followId);
        socialMapper.deleteFollow(userId);
        log.info("数据从数据库中清除");
        return Result.success();
    }


    @Override
    public Result listFollows(Long userId, Integer pageNum, Integer pageSize) {
        log.info("数据传入");
        List<UserDTO> list = socialMapper.listFollows(userId, pageNum, pageSize);
        if (pageNum > list.size() || pageSize > list.size()) {
            return Result.error("数据输入有误");
        }
        return Result.successWithTotal(list,socialMapper.listAllFollow(userId).size());
    }

    @Override
    public Result listFans(Long userId, Integer pageNum, Integer pageSize) {
        log.info("数据传入");
        List<UserDTO> list = socialMapper.listFans(userId, pageNum, pageSize);
        if (pageNum > list.size() || pageSize > list.size()) {
            return Result.error("数据输入有误");
        }
        return Result.successWithTotal(list,socialMapper.listAllFans(userId).size());
    }


    @Override
    public Result listAllFriend() {
        Long userId = securityContextHolderUtils.getUserId();
        List<Long> ids = new ArrayList();
        List<UserDTO> users = new ArrayList();
        for (UserDTO fan : socialMapper.listAllFollow(userId)) {
            log.info("数据传入");
            String key = "follows:" + fan.getId() + ":" + userId;
            if (redisTemplate.opsForSet().isMember(key, userId)) {
                ids.add(fan.getId());
            }
        }
        if (ids.isEmpty()) {
            return Result.error("你没有好友");
        }
        for (Long id : ids) {
            users.add(userMapper.queryUserDtoByUserId(id));
        }
        return Result.successWithTotal(users, users.size());
    }
}
