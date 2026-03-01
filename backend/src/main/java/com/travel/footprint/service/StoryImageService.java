package com.travel.footprint.service;

import com.travel.footprint.entity.StoryImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryImageService {
    
    String uploadImage(MultipartFile file);
    
    void saveStoryImages(Long storyId, List<StoryImage> images);
    
    List<StoryImage> getImagesByStoryId(Long storyId);
    
    void deleteImagesByStoryId(Long storyId);
    
    boolean deleteImage(String imageUrl);
}
