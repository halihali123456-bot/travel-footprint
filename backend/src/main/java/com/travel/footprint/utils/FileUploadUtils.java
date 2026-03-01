package com.travel.footprint.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadUtils {
    
    @Value("${upload.path}")
    private String uploadPath;
    
    @Value("${upload.access-path}")
    private String accessPath;
    
    /**
     * 上传文件
     */
    public String upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        
        // 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        
        // 保存文件
        File dest = new File(uploadDir, filename);
        file.transferTo(dest);
        
        return accessPath + filename;
    }
    
    /**
     * 删除文件
     */
    public boolean delete(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith(accessPath)) {
            return false;
        }
        
        String filename = fileUrl.substring(accessPath.length());
        File file = new File(uploadPath, filename);
        
        if (file.exists()) {
            return file.delete();
        }
        
        return false;
    }
}
