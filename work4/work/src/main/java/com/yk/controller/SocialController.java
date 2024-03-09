package com.yk.controller;

import com.yk.pojo.Result;
import com.yk.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 社交功能接口
 * @author 12080
 */
@RestController
public class SocialController {

    @Autowired
    private SocialService socialService;

    /**
     * 通过用户id关注用户
     *
     * @param toUserId   关注用户的id
     * @param actionType 1为关注 0为取关
     */
    @PostMapping("/relation/action")
    @PreAuthorize("hasAnyAuthority('relation')")
    public Result relationByUserId(@RequestParam(value = "to_user_id")Long toUserId,
                                   @RequestParam(value = "action_type") Integer actionType) {
        if (actionType == 0) {
            return socialService.saveFollowByUserId(toUserId);
        } else if (actionType == 1) {
            return socialService.deletedFollowByUserId(toUserId);
        }
        return Result.error("操作有误，请重试");
    }

    /**
     * 获取关注列表，可分页查询
     *
     * @param userId   用户id
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     */
    @GetMapping("/follow/list")
    @PreAuthorize("hasAnyAuthority('listFollow')")
    public Result listFollows(@RequestParam(value = "user_id")Long userId,
                              @RequestParam(value = "page_num") Integer pageNum,
                              @RequestParam(value = "page_size") Integer pageSize) {
        return socialService.listFollows(userId,pageNum,pageSize);
    }

    /**
     * 获取指定用户的粉丝列表，可分页查询
     *
     * @param userId   用户id
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     */
    @GetMapping("/fans/list")
    @PreAuthorize("hasAnyAuthority('listFans')")
    public Result listFans(@RequestParam(value = "user_id")Long userId,
                           @RequestParam(value = "page_num") Integer pageNum,
                           @RequestParam(value = "page_size") Integer pageSize) {
       return socialService.listFans(userId, pageNum, pageSize);
    }

    /**
     * 获取用户的好友列表
     */
    @GetMapping("/friend/list")
    @PreAuthorize("hasAnyAuthority('listFriends')")
    public Result listFriends() {
        return socialService.listAllFriend();
    }
}
