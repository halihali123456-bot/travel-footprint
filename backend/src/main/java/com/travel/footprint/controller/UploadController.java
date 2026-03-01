package com.travel.footprint.controller;

import com.travel.footprint.service.StoryImageService;
import com.travel.footprint.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {
    
    @Autowired
    private StoryImageService storyImageService;
    
    @PostMapping("/image")
    public ResultVO<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = storyImageService.uploadImage(file);
        
        Map<String, String> result = new HashMap<>();
        result.put("url", imageUrl);
        
        return ResultVO.success(result);
    }
}
