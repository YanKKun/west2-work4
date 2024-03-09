package com.yk.service;

import com.yk.pojo.Result;
import com.yk.pojo.User;
import org.springframework.stereotype.Service;

/**
 * 用户登录/登出业务层
 * @author 12080
 */
@Service
public interface LoginService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    Result login(String username,String password);

    /**
     * 用户登出
     * @return 结果
     */
    Result logout();
}
