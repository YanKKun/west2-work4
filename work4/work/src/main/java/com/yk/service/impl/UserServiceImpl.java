package com.yk.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.PutObjectRequest;
import com.yk.mapper.UserMapper;
import com.yk.pojo.Result;
import com.yk.pojo.User;
import com.yk.service.UserService;
import com.yk.utils.RedisCache;
import com.yk.utils.SecurityContextHolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 用户业务层实现类
 * @author 12080
 */
@Component
@Slf4j
public class UserServiceImpl implements UserService {


    private String bucketName="work-1324382741";

    private String path="https://work-1324382741.cos-website.ap-nanjing.myqcloud.com";

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityContextHolderUtils contextHolderUtils;

    @Autowired
    private COSClient cosClient;

    @Override
    public Result findByUserName(String username) {
        log.info("数据传入");
        if (userMapper.findByUserName(username)==null){
            return Result.error("查找不到用户");
        }
        log.info("操作成功");
        return Result.success(userMapper.findByUserName(username));
    }

    @Transactional
    @Override
    public Result register(String username, String password) {
        Long id = IdWorker.getId();
        //加密
        log.info("数据传入");
        String newPassword = passwordEncoder.encode(password);
        //查询登录账号
        User u = userMapper.findByUserName(username);
        if (u == null) {
            //没有占用
            //注册
            userMapper.saveUser(id, username, newPassword);
            userMapper.saveUserRole(id);
            log.info("数据已存入数据库");
            return Result.success();
        } else {
            //占用
            return Result.error("登录账号已被占用");
        }
    }

    @Override
    public Result<User> findByUserId(Long id) {
        log.info("数据传入");
        if (userMapper.findByUserId(id) == null) {
            return Result.error("用户不存在");
        }
        log.info("操作成功");
        return Result.success(userMapper.findByUserId(id));
    }

    @Transactional
    @Override
    public Result updateUserNickName(String nickname) {
        log.info("数据传入");
        Long userId = contextHolderUtils.getUserId();
        userMapper.updateUserNickName(nickname, contextHolderUtils.getUserId());
        log.info("数据已更新");
        return Result.success(userMapper.findByUserId(userId));
    }

    @Transactional
    @Override
    public Result updateUserAvatarUrl(MultipartFile data) throws IOException {
        log.info("数据传入");
        String originalFilename = data.getOriginalFilename();
        String dataType = FileUtil.extName(originalFilename);
        if("jpg".equals(dataType)||"jpeg".equals(dataType)||"png".equals(dataType)||"gif".equals(dataType)||"bmp".equals(dataType)||"tiff".equals(dataType)||"eps".equals(dataType)){
            String fileKey = getFileKey(originalFilename);
            InputStream inputStream = data.getInputStream();
            cosClient.putObject(new PutObjectRequest(bucketName,fileKey,inputStream,null));
            cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            String dataUrl = path+"/"+fileKey;
            log.info("数据上传到数据桶");
            userMapper.updateUserAvatarUrl(dataUrl, contextHolderUtils.getUserId());
            log.info("数据已存入数据库");
            cosClient.shutdown();
            return Result.success();
        }
        return Result.error("文件非图片");
    }

    private String getFileKey(String originalFileName) {
        String filePath = "userAvatar/";
        //1.获取后缀名 2.去除文件后缀 替换所有特殊字符
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileStr = StrUtil.removeSuffix(originalFileName, fileType).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "_");
        filePath +=  new DateTime().toString("yyyyMMddHHmmss") + "_" + fileStr + fileType;
        return filePath;
    }


    @Transactional
    @Override
    public Result updatePassword(String oldPwd,String newPwd,String rePwd) {
        log.info("数据传入");
        if (!passwordEncoder.matches(oldPwd, userMapper.findByUserName(contextHolderUtils.getUsername()).getPassword())) {
            return Result.error("旧密码错误");
        }
        if (!rePwd.equals(newPwd)) {
            return Result.error("两次填写的密码不一样");
        }
        if (newPwd.length() <= 5 || newPwd.length() >= 16) {
            return Result.error("新密码不符合规范");
        }
        userMapper.updatePassword(passwordEncoder.encode(newPwd), contextHolderUtils.getUserId());
        log.info("数据已存入数据库");
        return Result.success();
    }

    @Transactional
    @Override
    public Result deleteByUserId(Long userId) {
        log.info("数据传入");
        boolean flag = userMapper.deleteByUserId(userId);
        User user = userMapper.findByUserId(userId);
        if(flag&&user!=null){
            log.info("操作成功");
            return Result.success();
        }else {
            return Result.error("用户不存在或已被封禁");
        }

    }

    @Transactional
    @Override
    public Result unblockUserByUserId(Long userId) {
        log.info("数据传入");
        boolean flag = userMapper.unblockUserByUserId(userId);
        if(flag){
            return Result.success("已解封用户");
        }else {
            return Result.error("用户不存在或未被封禁");
        }
    }

    @Override
    public User findByUserName2(String username) {
        return userMapper.findByUserName(username);
    }
}
