package com.yk.mapper;

import com.yk.pojo.Result;
import com.yk.pojo.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 视频mapper层
 * @author 12080
 */
@Mapper
public interface VideoMapper {

    /**
     * 通过id返回视频
     * @param id 视频id
     * @return 视频
     */
    @Select("select id, user_id, video_url, cover_url, title, description, create_time, update_time, deleted_time, is_deleted, size, type, visit_count, like_count, comment_count from video where id = #{id}")
    Video queryVideoById(@Param("id") Integer id);

    /**
     * 投稿
     * @param type 视频文件后缀
     * @param size 视频大小
     * @param videoUrl 视频地址
     * @param title 视频标题
     * @param description 视频简介
     * @param userId 投稿人id
     */
    @Insert("insert into video set type=#{type},size=#{size}, video_url=#{video_url}, title = #{title},description = #{description},user_id=#{user_id},update_time=now(),create_time=now()")
    void uploadVideo(@Param("type") String type, @Param("size") Long size, @Param("video_url") String videoUrl, @Param("title") String title, @Param("description") String description, @Param("user_id") Long userId);

    /**
     * 查找指定用户的投稿列表
     * @param userId 投稿人id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 视频列表
     */
    List<Video> listPublishByUserId(@Param("user_id") Long userId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 获取用户投稿列表
     * @param userId 用户id
     * @return 结果
     */
    List<Video> listPublishByUserIdNonPage(Long userId);

    /**
     * 获取视频列表
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 视频列表
     */
    @Select("select id, user_id, video_url, cover_url, title, description, create_time, update_time, deleted_time,visit_count, like_count, comment_count from video where is_deleted =0 limit #{pageNum},#{pageSize}")
    List<Video> listVideo(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 通过关键字返回视频列表,可分页
     * @param keyword 关键字
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 视频列表
     */
    List<Video> listVideoByKeyword(@Param("keyword") String keyword, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 通过关键字返回视频列表
     * @param keyword 关键字
     * @return 视频列表
     */
    List<Video> listVideoByKeywordNonPage(@Param("keyword") String keyword);


    /**
     * 通过时间增加视频观看量
     * @param id 视频id
     * @param count 观看数
     */
    @Update("update video set visit_count= #{count} where id=#{id}")
    void updateVideoVisitCountByTime(@Param("id") Integer id, @Param("count") Integer count);

    /**
     * 删除登录用户指定视频返回成功与否
     * @param id 视频id
     * @param userId 用户id
     * @return 成功或失败
     */
    @Update("update video set is_deleted = 1 , deleted_time = now() where id = #{id} and is_deleted = 0 and user_id = #{user_id}")
    boolean deleteOwnVideoByVideoId(@Param("id")Integer id,@Param("user_id")Long userId);

    /**
     * 封禁指定视频返回成功与否
     * @param id 视频id
     * @return 成功或失败
     */
    @Update("update video set is_deleted = 1 , deleted_time = now() where id = #{id} and is_deleted = 0")
    boolean banVideoByVideoId(@Param("id")Integer id);
}
