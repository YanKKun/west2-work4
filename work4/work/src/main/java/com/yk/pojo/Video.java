package com.yk.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;


/**
 * 视频实体类
 * @author 120808
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    /**
     * 视频id
     */
    @TableId
    private Integer id;

    /**
     * 发布者id
     */
    private Long userId;

    /**
     * 视频路径
     */
    @URL
    private String videoUrl;

    /**
     * 封面路径
     */
    @URL
    private String coverUrl;

    /**
     * 标题
     */
    private String title;

    /**
     * 视频大小
     */
    @JsonIgnore
    private Long size;

    /**
     * 视频类型
     */
    @JsonIgnore
    private String type;

    /**
     * 视频是否删除/封禁
     */
    @JsonIgnore
    private Boolean isDeleted;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 点击量
     */
    private Integer visitCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 删除/封禁时间
     */
    private LocalDateTime deletedTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 视频简介
     */
    private String description;


}
