package com.yk.service;

import com.yk.pojo.Result;
import com.yk.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 用户业务层
 * @author 12080
 */
@Service
public interface UserService {

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 结果
     */
    Result findByUserName(String username);

    User findByUserName2(String username);

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    Result register(String username, String password);

    /**
     * 根据id查找用户
     * @param id 用户id
     * @return 结果
     */
    Result<User> findByUserId(Long id);

    /**
     * 用户更新昵称
     * @param nickname 昵称
     * @return 结果
     */
    Result updateUserNickName(String nickname);

    /**
     * 更新用户头像
     * @param data 数据
     * @return 结果
     * @throws IOException 数据异常签名
     */
    Result updateUserAvatarUrl(MultipartFile data) throws IOException;

    /**
     * 用户更新密码
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @param rePwd 重复新密码
     * @return 结果
     */
    Result updatePassword(String oldPwd,String newPwd,String rePwd);

    /**
     * 用户注销/封禁用户
     * @param userId 用户id
     * @return 结果
     */
    Result deleteByUserId(Long userId);

    /**
     * 解封用户
     * @param userId 用户id
     * @return 结果
     */
    Result unblockUserByUserId(Long userId);

}
