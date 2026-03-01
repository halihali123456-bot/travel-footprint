package com.travel.footprint.service.impl;

import com.travel.footprint.entity.StoryImage;
import com.travel.footprint.mapper.StoryImageMapper;
import com.travel.footprint.service.StoryImageService;
import com.travel.footprint.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StoryImageServiceImpl implements StoryImageService {
    
    @Autowired
    private FileUploadUtils fileUploadUtils;
    
    @Autowired
    private StoryImageMapper storyImageMapper;
    
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            return fileUploadUtils.upload(file);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
    
    @Override
    public void saveStoryImages(Long storyId, List<StoryImage> images) {
        if (images == null || images.isEmpty()) {
            return;
        }
        
        for (int i = 0; i < images.size(); i++) {
            StoryImage image = images.get(i);
            image.setStoryId(storyId);
            image.setSortOrder(i);
            storyImageMapper.insert(image);
        }
    }
    
    @Override
    public List<StoryImage> getImagesByStoryId(Long storyId) {
        return storyImageMapper.selectByStoryId(storyId);
    }
    
    @Override
    public void deleteImagesByStoryId(Long storyId) {
        storyImageMapper.deleteByStoryId(storyId);
    }
    
    @Override
    public boolean deleteImage(String imageUrl) {
        return fileUploadUtils.delete(imageUrl);
    }
}
