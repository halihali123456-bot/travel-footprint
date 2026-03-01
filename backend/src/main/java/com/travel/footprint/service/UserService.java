package com.travel.footprint.service;

import com.travel.footprint.dto.LoginDTO;
import com.travel.footprint.dto.RegisterDTO;
import com.travel.footprint.entity.User;
import com.travel.footprint.vo.ResultVO;
import com.travel.footprint.vo.UserVO;

public interface UserService {
    
    ResultVO<UserVO> login(LoginDTO loginDTO);
    
    ResultVO<UserVO> register(RegisterDTO registerDTO);
    
    ResultVO<UserVO> getUserInfo(Long userId);
    
    ResultVO<UserVO> updateUserInfo(Long userId, User user);
    
    User getById(Long userId);
}
