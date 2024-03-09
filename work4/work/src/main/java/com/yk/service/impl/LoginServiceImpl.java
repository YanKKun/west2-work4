package com.yk.service.impl;

import com.yk.mapper.UserMapper;
import com.yk.pojo.LoginUser;
import com.yk.pojo.Result;
import com.yk.pojo.User;
import com.yk.service.LoginService;
import com.yk.utils.JwtUtil;
import com.yk.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;

/**
 * 登录用户业务实现类
 * @author 12080
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisCache redisCache;

    @Transactional
    @Override
    public Result<String> login(String username , String password) {
        // AuthenticationManger authenticate 进行用户认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        log.info("运行中");
        // 如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);

        // 把完整的用户信息存入redis userid作为key
        redisCache.setCacheObject("login:" + userid, loginUser);
        log.info("数据存入redis");
        //根据用户名查询用户
        User user = userMapper.findByUserName(username);
        //判断该用户是否存在
        if (user == null) {
            return Result.error("用户名错误,或已被封禁");
        }
        //判断密码是否正确
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.error("密码错误");
        }
        log.info("登录成功");
        return Result.success(jwt);
    }

    @Transactional
    @Override
    public Result<String> logout() {
        // 获取SecurityContextHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();

        // 删除redis中的值
        redisCache.deleteObject("login:" + userid);
        log.info("redis中的数据已删除");

        return Result.success("退出成功");
    }

}
