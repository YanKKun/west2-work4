package com.yk.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "perm")
public class Permissions {

    /**
     * 权限id
     */
    @TableId
    private Integer id;

    /**
     * 权限名
     */
    private String permName;

    /**
     * 权限内容
     */
    private String perm;

    /**
     * 权限路径
     */
    private String path;

}
