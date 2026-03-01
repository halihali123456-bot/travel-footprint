package com.travel.footprint.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("story_like")
public class StoryLike {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long storyId;
    
    private Long userId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
