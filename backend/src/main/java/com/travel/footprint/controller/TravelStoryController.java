package com.travel.footprint.controller;

import com.travel.footprint.dto.StoryDTO;
import com.travel.footprint.entity.TravelStory;
import com.travel.footprint.service.TravelStoryService;
import com.travel.footprint.vo.PageVO;
import com.travel.footprint.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/story")
public class TravelStoryController {
    
    @Autowired
    private TravelStoryService travelStoryService;
    
    @PostMapping
    public ResultVO<TravelStory> createStory(@Validated @RequestBody StoryDTO storyDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return travelStoryService.createStory(userId, storyDTO);
    }
    
    @PutMapping("/{id}")
    public ResultVO<TravelStory> updateStory(@PathVariable Long id, @Validated @RequestBody StoryDTO storyDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        storyDTO.setId(id);
        return travelStoryService.updateStory(userId, storyDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResultVO<Void> deleteStory(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return travelStoryService.deleteStory(userId, id);
    }
    
    @GetMapping("/{id}")
    public ResultVO<TravelStory> getStoryById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return travelStoryService.getStoryById(id, userId);
    }
    
    @GetMapping("/public")
    public ResultVO<PageVO<TravelStory>> getPublicStories(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        return travelStoryService.getPublicStories(page, size);
    }
    
    @GetMapping("/my")
    public ResultVO<PageVO<TravelStory>> getMyStories(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return travelStoryService.getUserStories(userId, page, size);
    }
    
    @GetMapping("/user/{userId}")
    public ResultVO<PageVO<TravelStory>> getUserStories(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        return travelStoryService.getUserStories(userId, page, size);
    }
    
    @GetMapping("/search")
    public ResultVO<PageVO<TravelStory>> searchStories(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        return travelStoryService.searchStories(keyword, page, size);
    }
    
    @GetMapping("/hot")
    public ResultVO<List<TravelStory>> getHotStories(
            @RequestParam(defaultValue = "6") Integer limit) {
        return travelStoryService.getHotStories(limit);
    }
    
    @GetMapping("/timeline")
    public ResultVO<List<TravelStory>> getTimeline(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return travelStoryService.getTimeline(userId);
    }
    
    @PostMapping("/{id}/like")
    public ResultVO<Void> likeStory(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return travelStoryService.likeStory(userId, id);
    }
    
    @DeleteMapping("/{id}/like")
    public ResultVO<Void> unlikeStory(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return travelStoryService.unlikeStory(userId, id);
    }
}
