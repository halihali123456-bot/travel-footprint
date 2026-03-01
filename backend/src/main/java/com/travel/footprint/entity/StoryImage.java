package com.travel.footprint.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("story_image")
public class StoryImage {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long storyId;
    
    private String imageUrl;
    
    private String description;
    
    private Integer sortOrder;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
