package com.travel.footprint.dto;

import lombok.Data;

@Data
public class ImageDTO {
    
    private String imageUrl;
    
    private String description;
    
    private Integer sortOrder;
}
