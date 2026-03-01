package com.travel.footprint.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.footprint.dto.ImageDTO;
import com.travel.footprint.dto.StoryDTO;
import com.travel.footprint.entity.StoryImage;
import com.travel.footprint.entity.StoryLike;
import com.travel.footprint.entity.TravelStory;
import com.travel.footprint.mapper.StoryImageMapper;
import com.travel.footprint.mapper.StoryLikeMapper;
import com.travel.footprint.mapper.TravelStoryMapper;
import com.travel.footprint.service.TravelStoryService;
import com.travel.footprint.vo.PageVO;
import com.travel.footprint.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelStoryServiceImpl implements TravelStoryService {
    
    @Autowired
    private TravelStoryMapper travelStoryMapper;
    
    @Autowired
    private StoryImageMapper storyImageMapper;
    
    @Autowired
    private StoryLikeMapper storyLikeMapper;
    
    @Override
    @Transactional
    public ResultVO<TravelStory> createStory(Long userId, StoryDTO storyDTO) {
        TravelStory story = new TravelStory();
        BeanUtils.copyProperties(storyDTO, story);
        story.setUserId(userId);
        story.setViewCount(0);
        story.setLikeCount(0);
        
        travelStoryMapper.insert(story);
        
        // 保存图片
        if (storyDTO.getImages() != null && !storyDTO.getImages().isEmpty()) {
            List<StoryImage> images = new ArrayList<>();
            for (int i = 0; i < storyDTO.getImages().size(); i++) {
                ImageDTO imageDTO = storyDTO.getImages().get(i);
                StoryImage image = new StoryImage();
                image.setStoryId(story.getId());
                image.setImageUrl(imageDTO.getImageUrl());
                image.setDescription(imageDTO.getDescription());
                image.setSortOrder(i);
                images.add(image);
            }
            storyImageMapper.insertBatchSomeColumn(images);
        }
        
        return ResultVO.success(story);
    }
    
    @Override
    @Transactional
    public ResultVO<TravelStory> updateStory(Long userId, StoryDTO storyDTO) {
        TravelStory existStory = travelStoryMapper.selectById(storyDTO.getId());
        
        if (existStory == null) {
            return ResultVO.error("故事不存在");
        }
        
        if (!existStory.getUserId().equals(userId)) {
            return ResultVO.error("无权修改此故事");
        }
        
        TravelStory story = new TravelStory();
        BeanUtils.copyProperties(storyDTO, story);
        story.setUserId(null); // 不修改用户ID
        story.setViewCount(null); // 不修改浏览数
        story.setLikeCount(null); // 不修改点赞数
        
        travelStoryMapper.updateById(story);
        
        // 更新图片
        if (storyDTO.getImages() != null) {
            storyImageMapper.deleteByStoryId(story.getId());
            
            List<StoryImage> images = new ArrayList<>();
            for (int i = 0; i < storyDTO.getImages().size(); i++) {
                ImageDTO imageDTO = storyDTO.getImages().get(i);
                StoryImage image = new StoryImage();
                image.setStoryId(story.getId());
                image.setImageUrl(imageDTO.getImageUrl());
                image.setDescription(imageDTO.getDescription());
                image.setSortOrder(i);
                images.add(image);
            }
            
            if (!images.isEmpty()) {
                for (StoryImage image : images) {
                    storyImageMapper.insert(image);
                }
            }
        }
        
        return getStoryById(story.getId(), userId);
    }
    
    @Override
    @Transactional
    public ResultVO<Void> deleteStory(Long userId, Long storyId) {
        TravelStory story = travelStoryMapper.selectById(storyId);
        
        if (story == null) {
            return ResultVO.error("故事不存在");
        }
        
        if (!story.getUserId().equals(userId)) {
            return ResultVO.error("无权删除此故事");
        }
        
        // 删除图片
        storyImageMapper.deleteByStoryId(storyId);
        
        // 删除故事
        travelStoryMapper.deleteById(storyId);
        
        return ResultVO.success();
    }
    
    @Override
    public ResultVO<TravelStory> getStoryById(Long id, Long currentUserId) {
        TravelStory story = travelStoryMapper.selectByIdWithUser(id);
        
        if (story == null) {
            return ResultVO.error("故事不存在");
        }
        
        // 如果不是公开故事，且不是作者本人，则无权查看
        if (story.getIsPublic() != 1 && (currentUserId == null || !story.getUserId().equals(currentUserId))) {
            return ResultVO.error("无权查看此故事");
        }
        
        // 增加浏览次数
        travelStoryMapper.incrementViewCount(id);
        story.setViewCount(story.getViewCount() + 1);
        
        // 加载图片
        List<StoryImage> images = storyImageMapper.selectByStoryId(id);
        story.setImages(images);
        
        // 检查是否已点赞
        if (currentUserId != null) {
            StoryLike like = storyLikeMapper.selectByStoryIdAndUserId(id, currentUserId);
            story.setIsLiked(like != null);
        } else {
            story.setIsLiked(false);
        }
        
        return ResultVO.success(story);
    }
    
    @Override
    public ResultVO<PageVO<TravelStory>> getPublicStories(Long page, Long size) {
        Page<TravelStory> pageParam = new Page<>(page, size);
        Page<TravelStory> result = travelStoryMapper.selectPublicStories(pageParam);
        
        // 加载图片
        for (TravelStory story : result.getRecords()) {
            List<StoryImage> images = storyImageMapper.selectByStoryId(story.getId());
            story.setImages(images);
        }
        
        return ResultVO.success(new PageVO<>(result.getTotal(), page, size, result.getRecords()));
    }
    
    @Override
    public ResultVO<PageVO<TravelStory>> getUserStories(Long userId, Long page, Long size) {
        Page<TravelStory> pageParam = new Page<>(page, size);
        Page<TravelStory> result = travelStoryMapper.selectByUserId(pageParam, userId);
        
        // 加载图片
        for (TravelStory story : result.getRecords()) {
            List<StoryImage> images = storyImageMapper.selectByStoryId(story.getId());
            story.setImages(images);
        }
        
        return ResultVO.success(new PageVO<>(result.getTotal(), page, size, result.getRecords()));
    }
    
    @Override
    public ResultVO<PageVO<TravelStory>> searchStories(String keyword, Long page, Long size) {
        Page<TravelStory> pageParam = new Page<>(page, size);
        Page<TravelStory> result = travelStoryMapper.searchStories(pageParam, keyword);
        
        // 加载图片
        for (TravelStory story : result.getRecords()) {
            List<StoryImage> images = storyImageMapper.selectByStoryId(story.getId());
            story.setImages(images);
        }
        
        return ResultVO.success(new PageVO<>(result.getTotal(), page, size, result.getRecords()));
    }
    
    @Override
    public ResultVO<List<TravelStory>> getHotStories(Integer limit) {
        List<TravelStory> stories = travelStoryMapper.selectHotStories(limit);
        
        // 加载图片
        for (TravelStory story : stories) {
            List<StoryImage> images = storyImageMapper.selectByStoryId(story.getId());
            story.setImages(images);
        }
        
        return ResultVO.success(stories);
    }
    
    @Override
    public ResultVO<List<TravelStory>> getTimeline(Long userId) {
        Page<TravelStory> pageParam = new Page<>(1, 1000);
        Page<TravelStory> result = travelStoryMapper.selectByUserId(pageParam, userId);
        
        List<TravelStory> stories = result.getRecords();
        
        // 按旅行日期排序
        stories = stories.stream()
                .sorted(Comparator.comparing(TravelStory::getTravelDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        
        // 加载图片
        for (TravelStory story : stories) {
            List<StoryImage> images = storyImageMapper.selectByStoryId(story.getId());
            story.setImages(images);
        }
        
        return ResultVO.success(stories);
    }
    
    @Override
    @Transactional
    public ResultVO<Void> likeStory(Long userId, Long storyId) {
        StoryLike existLike = storyLikeMapper.selectByStoryIdAndUserId(storyId, userId);
        
        if (existLike != null) {
            return ResultVO.error("已经点赞过了");
        }
        
        StoryLike like = new StoryLike();
        like.setStoryId(storyId);
        like.setUserId(userId);
        storyLikeMapper.insert(like);
        
        travelStoryMapper.incrementLikeCount(storyId);
        
        return ResultVO.success();
    }
    
    @Override
    @Transactional
    public ResultVO<Void> unlikeStory(Long userId, Long storyId) {
        StoryLike existLike = storyLikeMapper.selectByStoryIdAndUserId(storyId, userId);
        
        if (existLike == null) {
            return ResultVO.error("还没有点赞");
        }
        
        storyLikeMapper.deleteById(existLike.getId());
        travelStoryMapper.decrementLikeCount(storyId);
        
        return ResultVO.success();
    }
}
