package com.travel.footprint.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.footprint.entity.TravelStory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TravelStoryMapper extends BaseMapper<TravelStory> {
    
    @Select("SELECT s.*, u.username, u.avatar as user_avatar " +
            "FROM travel_story s " +
            "LEFT JOIN user u ON s.user_id = u.id " +
            "WHERE s.is_public = 1 " +
            "ORDER BY s.create_time DESC")
    @Results({
        @Result(property = "userAvatar", column = "user_avatar")
    })
    Page<TravelStory> selectPublicStories(Page<TravelStory> page);
    
    @Select("SELECT s.*, u.username, u.avatar as user_avatar " +
            "FROM travel_story s " +
            "LEFT JOIN user u ON s.user_id = u.id " +
            "WHERE s.user_id = #{userId} " +
            "ORDER BY s.travel_date DESC")
    @Results({
        @Result(property = "userAvatar", column = "user_avatar")
    })
    Page<TravelStory> selectByUserId(Page<TravelStory> page, Long userId);
    
    @Select("SELECT s.*, u.username, u.avatar as user_avatar " +
            "FROM travel_story s " +
            "LEFT JOIN user u ON s.user_id = u.id " +
            "WHERE s.id = #{id}")
    @Results({
        @Result(property = "userAvatar", column = "user_avatar")
    })
    TravelStory selectByIdWithUser(Long id);
    
    @Select("SELECT s.*, u.username, u.avatar as user_avatar " +
            "FROM travel_story s " +
            "LEFT JOIN user u ON s.user_id = u.id " +
            "WHERE s.is_public = 1 AND (s.title LIKE CONCAT('%', #{keyword}, '%') OR s.content LIKE CONCAT('%', #{keyword}, '%') OR s.location LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY s.create_time DESC")
    @Results({
        @Result(property = "userAvatar", column = "user_avatar")
    })
    Page<TravelStory> searchStories(Page<TravelStory> page, String keyword);
    
    @Update("UPDATE travel_story SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(Long id);
    
    @Update("UPDATE travel_story SET like_count = like_count + 1 WHERE id = #{id}")
    void incrementLikeCount(Long id);
    
    @Update("UPDATE travel_story SET like_count = like_count - 1 WHERE id = #{id}")
    void decrementLikeCount(Long id);
    
    @Select("SELECT s.*, u.username, u.avatar as user_avatar " +
            "FROM travel_story s " +
            "LEFT JOIN user u ON s.user_id = u.id " +
            "WHERE s.is_public = 1 " +
            "ORDER BY s.like_count DESC " +
            "LIMIT #{limit}")
    @Results({
        @Result(property = "userAvatar", column = "user_avatar")
    })
    List<TravelStory> selectHotStories(Integer limit);
}
