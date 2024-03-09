package com.yk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yk.dto.UserDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 12080
 */
@Mapper
public interface SocialMapper extends BaseMapper {

    /**
     * 增加指定用户粉丝数
     * @param userId 用户id
     */
    @Update("update user set fans_count = fans_count +1 where id = #{id}")
    void saveFans(@Param("id") Long userId);

    /**
     * 减少指定用户粉丝数
     * @param userId 用户id
     */
    @Update("update user set fans_count = fans_count -1 where id = #{id}")
    void deleteFans(@Param("id") Long userId);

    /**
     * 增加指定用户关注数
     * @param userId 用户id
     */
    @Update("update user set follow_count = follow_count +1 where id = #{id}")
    void saveFollow(@Param("id") Long userId);
    /**
     * 减少指定用户关注数
     * @param userId 用户id
     */
    @Update("update user set follow_count = follow_count -1 where id = #{id}")
    void deleteFollow(@Param("id") Long userId);

    /**
     * 将用户id和被关注用户id添加到中间表
     * @param userId 用户id
     * @param followId 被关注用户id
     */
    @Insert("insert into user_follows set user_id = #{user_id}, follow_id = #{follow_id} ")
    void saveFollowByUserId(@Param("user_id") Long userId, @Param("follow_id") Long followId);

    /**
     * 将用户id和被关注用户id从中间表删除
     * @param userId 用户id
     * @param followId 被关注用户id
     */
    @Delete("delete from user_follows where user_id = #{user_id} and follow_id = #{follow_id}")
    void deletedFollowByUserId(@Param("user_id") Long userId, @Param("follow_id") Long followId);

    /**
     * 通过用户id和被关注用户id返回被关注用户粉丝
     * @param userId 用户id
     * @param followId 被关注用户id
     * @return 数量
     */
    @Select("select user_id from user_follows where user_id = #{user_id} and follow_id = #{follow_id}")
    List<String> findUserAndFollowById(@Param("user_id") Long userId, @Param("follow_id") Long followId);

    /**
     * 返回指定用户关注列表，可分页
     * @param userId 用户id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 用户DTO
     */
    List<UserDTO> listFollows(@Param("user_id") Long userId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 返回指定用户粉丝列表，可分页
     * @param userId 用户id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 用户DTO
     */
    List<UserDTO> listFans(@Param("follow_id") Long userId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 返回指定用户全部关注列表
     * @param userId 用户id
     * @return 用户DTO
     */
    List<UserDTO> listAllFollow(@Param("user_id") Long userId);

    /**
     * 返回指定用户全部关注列表
     * @param userId 用户id
     * @return 用户DTO
     */
    List<UserDTO> listAllFans(@Param("follow_id") Long userId);

}
