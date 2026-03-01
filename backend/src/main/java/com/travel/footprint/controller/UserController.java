package com.travel.footprint.controller;

import com.travel.footprint.dto.LoginDTO;
import com.travel.footprint.dto.RegisterDTO;
import com.travel.footprint.entity.User;
import com.travel.footprint.service.UserService;
import com.travel.footprint.vo.ResultVO;
import com.travel.footprint.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResultVO<UserVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }
    
    @PostMapping("/register")
    public ResultVO<UserVO> register(@Validated @RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }
    
    @GetMapping("/info")
    public ResultVO<UserVO> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getUserInfo(userId);
    }
    
    @PutMapping("/info")
    public ResultVO<UserVO> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.updateUserInfo(userId, user);
    }
}
