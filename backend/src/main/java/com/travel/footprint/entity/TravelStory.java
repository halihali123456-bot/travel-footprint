package com.travel.footprint.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("travel_story")
public class TravelStory {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String title;
    
    private String content;
    
    private String location;
    
    private Double latitude;
    
    private Double longitude;
    
    private LocalDate travelDate;
    
    private String coverImage;
    
    private Integer isPublic;
    
    private Integer viewCount;
    
    private Integer likeCount;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    // 非数据库字段
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String userAvatar;
    
    @TableField(exist = false)
    private List<StoryImage> images;
    
    @TableField(exist = false)
    private Boolean isLiked;
}
