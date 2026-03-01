-- 旅行足迹记录与分享平台数据库设计
-- 数据库: travel_footprint

CREATE DATABASE IF NOT EXISTS travel_footprint DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE travel_footprint;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 旅行故事表
CREATE TABLE IF NOT EXISTS `travel_story` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '故事ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `location` VARCHAR(200) DEFAULT NULL COMMENT '地点',
    `latitude` DECIMAL(10, 8) DEFAULT NULL COMMENT '纬度',
    `longitude` DECIMAL(11, 8) DEFAULT NULL COMMENT '经度',
    `travel_date` DATE DEFAULT NULL COMMENT '旅行日期',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片',
    `is_public` TINYINT(1) DEFAULT 1 COMMENT '是否公开：0-私密，1-公开',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT DEFAULT 0 COMMENT '点赞次数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_travel_date` (`travel_date`),
    KEY `idx_is_public` (`is_public`),
    CONSTRAINT `fk_story_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='旅行故事表';

-- 故事图片表
CREATE TABLE IF NOT EXISTS `story_image` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
    `story_id` BIGINT NOT NULL COMMENT '故事ID',
    `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '图片描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_story_id` (`story_id`),
    CONSTRAINT `fk_image_story` FOREIGN KEY (`story_id`) REFERENCES `travel_story` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事图片表';

-- 点赞记录表
CREATE TABLE IF NOT EXISTS `story_like` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `story_id` BIGINT NOT NULL COMMENT '故事ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_story_user` (`story_id`, `user_id`),
    KEY `idx_story_id` (`story_id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_like_story` FOREIGN KEY (`story_id`) REFERENCES `travel_story` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞记录表';

-- 插入测试数据
INSERT INTO `user` (`username`, `password`, `nickname`, `avatar`, `email`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '管理员', NULL, 'admin@example.com'),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '旅行者小王', NULL, 'user1@example.com'),
('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '背包客小李', NULL, 'user2@example.com');

INSERT INTO `travel_story` (`user_id`, `title`, `content`, `location`, `latitude`, `longitude`, `travel_date`, `cover_image`, `is_public`, `view_count`, `like_count`) VALUES
(2, '西湖一日游', '今天来到了美丽的杭州西湖，湖水清澈，风景如画。断桥残雪、苏堤春晓，每一处都让人流连忘返。', '杭州西湖', 30.2489, 120.1460, '2024-03-15', NULL, 1, 128, 25),
(2, '黄山云海', '黄山的云海真的太壮观了！清晨五点起床，爬到光明顶，看到了绝美的日出和云海。', '安徽黄山', 30.1334, 118.1678, '2024-02-20', NULL, 1, 256, 45),
(3, '厦门鼓浪屿', '鼓浪屿的建筑很有特色，漫步在小巷中，感受历史的痕迹。海风吹拂，很是惬意。', '厦门鼓浪屿', 24.4465, 118.0823, '2024-01-10', NULL, 1, 89, 12),
(2, '成都美食之旅', '来到成都，一定要尝尝正宗的火锅和串串香！辣得过瘾，香得难忘。', '四川成都', 30.5728, 104.0668, '2024-03-01', NULL, 1, 167, 38);

INSERT INTO `story_image` (`story_id`, `image_url`, `description`, `sort_order`) VALUES
(1, '/uploads/story1_1.jpg', '西湖断桥', 1),
(1, '/uploads/story1_2.jpg', '苏堤春晓', 2),
(2, '/uploads/story2_1.jpg', '黄山云海', 1),
(3, '/uploads/story3_1.jpg', '鼓浪屿建筑', 1),
(4, '/uploads/story4_1.jpg', '成都火锅', 1);
