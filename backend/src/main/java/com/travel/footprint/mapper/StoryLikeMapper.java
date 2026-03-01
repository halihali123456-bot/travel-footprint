package com.travel.footprint.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.footprint.entity.StoryLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StoryLikeMapper extends BaseMapper<StoryLike> {
    
    @Select("SELECT * FROM story_like WHERE story_id = #{storyId} AND user_id = #{userId}")
    StoryLike selectByStoryIdAndUserId(Long storyId, Long userId);
    
    @Select("SELECT COUNT(*) FROM story_like WHERE story_id = #{storyId}")
    Integer countByStoryId(Long storyId);
}
