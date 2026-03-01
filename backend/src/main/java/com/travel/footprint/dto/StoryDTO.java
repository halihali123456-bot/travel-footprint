package com.travel.footprint.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
public class StoryDTO {
    
    private Long id;
    
    @NotBlank(message = "标题不能为空")
    private String title;
    
    private String content;
    
    private String location;
    
    private Double latitude;
    
    private Double longitude;
    
    private LocalDate travelDate;
    
    private String coverImage;
    
    private Integer isPublic;
    
    private List<ImageDTO> images;
}
