package com.yk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yk.dto.UserDTO;
import com.yk.pojo.User;
import org.apache.ibatis.annotations.*;

/**
 * 用户mapper层
 * @author 12080
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过用户名返回对应用户
     * @param username 用户名
     * @return 用户
     */
    @Select("select username , password , id from user where username = #{username} and is_deleted = 0")
    User findByUserName(@Param("username") String username);

    /**
     * 新建用户
     * @param id 用户id
     * @param username 用户名
     * @param password 密码
     */
    @Insert("insert into user set id=#{id},username=#{username},password=#{password},create_time=now(),update_time=now()")
    void saveUser(@Param("id") Long id, @Param("username") String username, @Param("password") String password);


    /**
     * 通过用户id返回指定用户
     * @param id 用户id
     * @return 用户
     */
    @Select("select username,id,nickname,create_time,avatar_url,update_time,follow_count,fans_count from user where id = #{id}")
    User findByUserId(@Param("id") Long id);

    /**
     * 通过id返回指定用户DTO
     * @param id 用户id
     * @return 用户DTO
     */
    @Select("select id,nickname,create_time,avatar_url from user where id = #{id}")
    UserDTO queryUserDtoByUserId(@Param("id") Long id);

    /**
     * 通过id指定用户角色
     * @param id 用户id
     */
    @Insert("insert  into user_role set uid=#{id},rid=1")
    void saveUserRole(@Param("id") Long id);

    /**
     * 修改登录用户昵称
     * @param nickname 昵称
     * @param id 用户id
     */
    @Update("update user set nickname=#{nickname},update_time=now() where id =  #{id} ")
    void updateUserNickName(@Param("nickname") String nickname,@Param("id") Long id);

    /**
     * 更新用户头像
     * @param avatarUrl 头像路径
     * @param id 用户id
     */
    @Update("update user set avatar_url = #{avatar_url} ,update_time=now() where id = #{id}")
    void updateUserAvatarUrl(@Param("avatar_url") String avatarUrl, @Param("id") Long id);

    /**
     * 更新登录用户密码
     * @param password 密码
     * @param id 用户id
     */
    @Update("update user set password = #{password} where id = #{id}")
    void updatePassword(@Param("password") String password, @Param("id") Long id);

    /**
     * 注销登录用户/封禁指定用户
     *
     * @param id 用户id
     * @return
     */
    @Update("update  user set is_deleted = 1 ,deleted_time = now() where id =#{id} and is_deleted = 0")
    boolean deleteByUserId(@Param("id") Long id);

    /**
     * 解封用户
     * @param id 用户id
     * @return 成功或失败
     */
    @Update("update  user set is_deleted = 0 , deleted_time=null where id =#{id} and is_deleted = 1")
    boolean unblockUserByUserId(@Param("id")Long id);

}
