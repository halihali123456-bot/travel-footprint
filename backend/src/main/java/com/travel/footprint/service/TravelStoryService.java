package com.travel.footprint.service;

import com.travel.footprint.dto.StoryDTO;
import com.travel.footprint.entity.TravelStory;
import com.travel.footprint.vo.PageVO;
import com.travel.footprint.vo.ResultVO;

import java.util.List;

public interface TravelStoryService {
    
    ResultVO<TravelStory> createStory(Long userId, StoryDTO storyDTO);
    
    ResultVO<TravelStory> updateStory(Long userId, StoryDTO storyDTO);
    
    ResultVO<Void> deleteStory(Long userId, Long storyId);
    
    ResultVO<TravelStory> getStoryById(Long id, Long currentUserId);
    
    ResultVO<PageVO<TravelStory>> getPublicStories(Long page, Long size);
    
    ResultVO<PageVO<TravelStory>> getUserStories(Long userId, Long page, Long size);
    
    ResultVO<PageVO<TravelStory>> searchStories(String keyword, Long page, Long size);
    
    ResultVO<List<TravelStory>> getHotStories(Integer limit);
    
    ResultVO<List<TravelStory>> getTimeline(Long userId);
    
    ResultVO<Void> likeStory(Long userId, Long storyId);
    
    ResultVO<Void> unlikeStory(Long userId, Long storyId);
}
