package com.travel.footprint;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.travel.footprint.mapper")
public class TravelFootprintApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelFootprintApplication.class, args);
        System.out.println("========================================");
        System.out.println("旅行足迹记录与分享平台启动成功！");
        System.out.println("接口地址: http://localhost:8080/api");
        System.out.println("========================================");
    }
}
