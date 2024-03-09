package com.yk.service;

import com.yk.dto.UserDTO;
import com.yk.pojo.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 社交业务层
 * @author 12080
 */
@Service
public interface SocialService {


    /**
     * 保存用户关注的对象
     * @param followId 关注者id
     * @return 结果
     */
    Result saveFollowByUserId(Long followId);

    /**
     * 删除用户关注的对象
     * @param followId 关注者的id
     * @return 结果
     */
    Result deletedFollowByUserId(Long followId);

    /**
     * 列出指定用户的关注列表，可分页
     * @param userId 用户id
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @return 结果
     */
    Result listFollows(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 列出指定用户的粉丝列表，可分页
     * @param userId 用户id
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @return 结果
     */
    Result listFans(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 列出指定用户的好友列表
     * @return 结果
     */
    Result listAllFriend();
}
