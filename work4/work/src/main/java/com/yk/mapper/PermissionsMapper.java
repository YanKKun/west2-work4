package com.yk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yk.pojo.Permissions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 权限mapper层
 * @author 120808
 */
@Mapper
public interface PermissionsMapper extends BaseMapper<Permissions> {
    /**
     * @param id 权限id
     * @return 权限列表
     */
    List<String> selectPermsByUserId(Long id);
}
