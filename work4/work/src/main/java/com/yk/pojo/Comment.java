package com.yk.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论实体类
 * @author 12080
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    /**
     * 评论id
     */
    @TableId
    private Integer id;

    /**
     * 所属视频id
     */
    @TableField(value = "video_id")
    private Integer videoId;

    /**
     * 评论内容
     */
    private String context;

    /**
     * 发布者id
     */
    private Long userId;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 子评论数
     */
    private Integer childCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * 是否被删除
     */
    @JsonIgnore
    private Boolean isDeleted = false;

}
