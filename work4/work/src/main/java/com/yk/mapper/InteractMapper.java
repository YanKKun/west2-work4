package com.yk.mapper;

import com.yk.pojo.Comment;
import com.yk.pojo.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 互动mapper层
 * @author
 */
@Mapper
public interface InteractMapper {

    /**
     * 保存视频点赞
     * @param videoId 视频id
     * @return 成功或失败
     */
    @Update("update video set like_count = like_count + 1 where id = #{id}")
    boolean saveVideoLike(@Param("id") Integer videoId);

    /**
     * 删除视频点赞
     * @param videoId 视频id
     * @return 成功或失败
     */
    @Update("update video set like_count = like_count - 1 where id = #{id}")
    boolean deleteVideoLike(@Param("id") Integer videoId);

    /**
     * 保存评论点赞
     * @param id 评论id
     * @return 成功或失败
     */
    @Update("update comment set like_count = like_count + 1 where id = #{id}")
    boolean saveCommentLike(@Param("id") Integer id);

    /**
     * 删除评论点赞
     * @param id 评论id
     * @return 成功或失败
     */
    @Update("update comment set like_count = like_count - 1 where id = #{id}")
    boolean deleteCommentLike(@Param("id") Integer id);

    /**
     * 保存用户和点赞视频到中间表
     * @param userId 用户id
     * @param videoId 视频id
     */
    @Insert("insert into `like` set user_id = #{user_id},video_id = #{video_id}")
    void saveVideoLikeOnMid(@Param("user_id") Long userId, @Param("video_id") Integer videoId);

    /**
     * 保存用户和点赞评论到中间表
     * @param userId 用户id
     * @param commentId 评论id
     */
    @Insert("insert into `like` set user_id = #{user_id},comment_id = #{comment_id}")
    void saveCommentLikeOnMid(@Param("user_id") Long userId, @Param("comment_id") Integer commentId);

    /**
     * 从中间表删除用户和点赞视频
     * @param userId 用户id
     * @param videoId 视频id
     */
    @Delete("delete from `like` where user_id = #{user_id} and video_id = #{video_id}")
    void deleteVideoLikeOnMid(@Param("user_id") Long userId, @Param("video_id") Integer videoId);

    /**
     * 从中间表删除用户和点赞评论
     * @param userId 用户id
     * @param commentId 评论id
     */
    @Delete("delete from `like` where user_id = #{user_id} and comment_id = #{comment_id}")
    void deleteCommentLikeOnMid(@Param("user_id") Long userId, @Param("comment_id") Integer commentId);

    /**
     * 获取指定用户点赞视频列表，可分页
     * @param userId 用户id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 视频列表
     */
    List<Video> listVideoByUserLike(@Param("user_id") Long userId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);


    /**
     * 获取指定用户所有点赞视频列表
     * @param userId 用户id
     * @return 视频列表
     */
    List<Video> listAllVideoByUserLike(@Param("user_id") Long userId);


    /**
     * 通过id返回指定评论
     * @param commentId 评论id
     * @return 评论
     */
    @Select("select id,user_id,parent_id,video_id from comment where id = #{id}")
    Comment findCommentById(@Param("id") Integer commentId);


    /**
     * 发布评论
     * @param videoId 视频id
     * @param context 内容
     * @param userId 发布者
     */
    @Insert("insert into comment set video_id = #{video_id} , user_id = #{user_id},context = #{context}, update_time = now(),create_time = now()")
    void saveVideoComment(@Param("video_id") Integer videoId, @Param("context") String context, @Param("user_id") Long userId);

    /**
     * 增加视频评论量
     * @param videoId 视频id
     */
    @Update("update video set comment_count = comment_count +1 WHERE id  = #{id}")
    void saveVideoCommentCount(@Param("id") Integer videoId);

    /**
     * 删除指定评论
     * @param videoId 视频id
     * @param userId 发布者id
     * @param commentId 评论id
     * @return 成功或失败
     */
    @Update("update comment set is_deleted = 1 ,deleted_time = now() where video_id = #{video_id} and user_id = #{user_id} and id = #{id}")
    boolean deleteComment(@Param("video_id") Integer videoId, @Param("user_id") Long userId, @Param("id") Integer commentId);

    /**
     * 减少指定视频评论量
     * @param videoId 视频id
     */
    @Update("update video set comment_count = comment_count -1 WHERE id  = #{id}")
    void updateVideoCommentCount(@Param("id") Integer videoId);

    /**
     * 发布子评论
     * @param videoId 视频id
     * @param userId 发布者id
     * @param toCommentId 父评论id
     * @param context 评论内容
     */
    @Insert("insert into comment set video_id = #{video_id},user_id = #{user_id},parent_id = #{parent_id},context = #{context},create_time= now(), update_time = now()")
    void saveChildComment(@Param("video_id") Integer videoId, @Param("user_id") Long userId, @Param("parent_id") Integer toCommentId, @Param("context") String context);

    /**
     * 增加父评论子评论数
     * @param toCommentId 父评论id
     */
    @Update("update comment set child_count = child_count + 1 where id = #{id}")
    void saveChildCommentCount(@Param("id") Integer toCommentId);

    /**
     * 减少父评论子评论数
     * @param toCommentId 父评论id
     */
    @Update("update comment set child_count = child_count - 1 where id = #{id}")
    void updateChildCommentCount(@Param("id") Integer toCommentId);

    /**
     * 返回指定视频的所有评论可分页
     * @param videoId 视频id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 评论列表
     */
    @Select("select id, user_id, video_id, context, parent_id, like_count, child_count, create_time, update_time, deleted_time from comment where video_id = #{video_id} and is_deleted=0 limit #{pageNum},#{pageSize}")
    List<Comment> listAllVideoComment(@Param("video_id") Integer videoId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 返回指定视频的所有评论
     * @param videoId 视频id
     * @return 评论列表
     */
    @Select("select id, user_id, video_id, context, parent_id, like_count, child_count, create_time, update_time, deleted_time from comment where video_id = #{video_id} and is_deleted=0")
    List<Comment> listAllVideoCommentNoPage(@Param("video_id") Integer videoId);


    /**
     * 返回指定评论的所有子评论，可分页
     * @param parentId 父评论id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return 评论列表
     */
    @Select("select id, user_id, video_id, context, parent_id, like_count, child_count, create_time, update_time, deleted_time from comment where parent_id = #{parent_id} and is_deleted=0 limit #{pageNum},#{pageSize}")
    List<Comment> listAllChildComment(@Param("parent_id") Integer parentId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 返回指定评论的所有子评论
     * @param parentId 父评论id
     * @return 评论列表
     */
    @Select("select id, user_id, video_id, context, parent_id, like_count, child_count, create_time, update_time, deleted_time from comment where parent_id = #{parent_id} and is_deleted=0")
    List<Comment> listAllChildCommentNoPage(@Param("parent_id") Integer parentId);

}
