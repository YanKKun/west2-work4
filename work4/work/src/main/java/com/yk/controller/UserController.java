package com.yk.controller;

import com.yk.pojo.Result;
import com.yk.pojo.User;
import com.yk.service.LoginService;
import com.yk.service.UserService;
import com.yk.utils.SecurityContextHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.io.IOException;

/**
 * 用户操作接口
 * @author 12080
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;


    /**
     * 注册新用户
     * @param username 用户名
     * @param password 密码
     */
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,
                           @Pattern(regexp = "^\\S{5,16}$") String password) {
        return userService.register(username,password);
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     */
    @PostMapping("/login")
    public Result login(@RequestParam @Pattern(regexp = "^\\S{5,16}$") String username,
                        @RequestParam@Pattern(regexp = "^\\S{5,16}$") String password) {

        return loginService.login(username , password);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result logout() {
        return loginService.logout();
    }

    /**
     * 通过用户id查找指定用户信息
     * @param userId 用户id
     */
    @GetMapping("/userInfo")
    @PreAuthorize("hasAnyAuthority('getinfo')")
    public Result<User> findByUserId(@RequestParam(value = "user_id")Long userId) {
        return userService.findByUserId(userId);
    }

    /**
     * 更新登录用户昵称
     *
     * @param nickname 昵称
     */
    @PutMapping("/update/nickName")
    @PreAuthorize("hasAnyAuthority('nickname')")
    public Result updateUserNickName(@Pattern(regexp = "^\\S{2,16}$") String nickname) {
        return userService.updateUserNickName(nickname);
    }

    /**
     * 修改用户头像
     *
     * @param data 图片文件
     * @throws IOException 文件传输异常
     */
    @PutMapping("/update/avatarUrl")
    @PreAuthorize("hasAnyAuthority('avatar')")
    public Result updateUserAvatarUrl(MultipartFile data) throws IOException {
        return userService.updateUserAvatarUrl(data);
    }

    /**
     * 修改登录用户密码
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @param rePwd  重复新密码
     */
    @PutMapping("/update/pwd")
    @PreAuthorize("hasAnyAuthority('pwd')")
    public Result updatePassword(@RequestParam(value = "old_pwd")String oldPwd,
                                 @RequestParam(value = "new_pwd") String newPwd,
                                 @RequestParam(value = "re_pwd") String rePwd) {
        return userService.updatePassword(oldPwd,newPwd,rePwd);
    }

    /**
     * 封禁指定用户
     * @param userId 用户id
     */
    @PostMapping("/banUser")
    @PreAuthorize("hasAnyAuthority('banUser')")
    public Result banUser(@RequestParam(value = "user_id")Long userId){
       return userService.deleteByUserId(userId);
    }

    /**
     * 解封指定用户
     * @param userId 用户id
     */
    @PostMapping("/unblockUser")
    @PreAuthorize("hasAnyAuthority('unblockUser')")
    public Result unblockUser(@RequestParam(value = "user_id")Long userId){
        return userService.unblockUserByUserId(userId);
    }
}
