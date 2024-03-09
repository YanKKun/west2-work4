package com.yk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * @author 12080
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@TableName(value = "user")
public class User {

    /**
     * 用户名
     */
    private String username;


    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @NotNull
    private Long id;

    /**
     * 用户头像路径
     */
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 用户昵称
     */
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname;

    /**
     * 用户创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户是否注销/封禁
     */
    @JsonIgnore
    private Boolean isDeleted = false;

    /**
     * 用户注销/被封禁时间
     */
    @TableField(value = "deleted_time")
    private LocalDateTime deletedTime;

    /**
     * 关注数
     */
    private Integer followCount;

    /**
     * 粉丝数
     */
    private Integer fansCount;


}
