package com.travel.footprint.vo;

import lombok.Data;

@Data
public class UserVO {
    
    private Long id;
    
    private String username;
    
    private String nickname;
    
    private String avatar;
    
    private String email;
    
    private String token;
}
