package com.travel.footprint.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.footprint.entity.StoryImage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StoryImageMapper extends BaseMapper<StoryImage> {
    
    @Select("SELECT * FROM story_image WHERE story_id = #{storyId} ORDER BY sort_order")
    List<StoryImage> selectByStoryId(Long storyId);
    
    @Delete("DELETE FROM story_image WHERE story_id = #{storyId}")
    void deleteByStoryId(Long storyId);
}
