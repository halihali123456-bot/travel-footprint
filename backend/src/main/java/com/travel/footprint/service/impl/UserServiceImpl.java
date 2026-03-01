package com.travel.footprint.service.impl;

import com.travel.footprint.dto.LoginDTO;
import com.travel.footprint.dto.RegisterDTO;
import com.travel.footprint.entity.User;
import com.travel.footprint.mapper.UserMapper;
import com.travel.footprint.service.UserService;
import com.travel.footprint.utils.JwtUtils;
import com.travel.footprint.utils.PasswordUtils;
import com.travel.footprint.vo.ResultVO;
import com.travel.footprint.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordUtils passwordUtils;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public ResultVO<UserVO> login(LoginDTO loginDTO) {
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        
        if (user == null) {
            return ResultVO.error("用户名或密码错误");
        }
        
        if (!passwordUtils.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResultVO.error("用户名或密码错误");
        }
        
        UserVO userVO = convertToVO(user);
        userVO.setToken(jwtUtils.generateToken(user.getId(), user.getUsername()));
        
        return ResultVO.success(userVO);
    }
    
    @Override
    public ResultVO<UserVO> register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(registerDTO.getUsername());
        if (existUser != null) {
            return ResultVO.error("用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordUtils.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setEmail(registerDTO.getEmail());
        
        userMapper.insert(user);
        
        UserVO userVO = convertToVO(user);
        userVO.setToken(jwtUtils.generateToken(user.getId(), user.getUsername()));
        
        return ResultVO.success(userVO);
    }
    
    @Override
    public ResultVO<UserVO> getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }
        
        return ResultVO.success(convertToVO(user));
    }
    
    @Override
    public ResultVO<UserVO> updateUserInfo(Long userId, User user) {
        User existUser = userMapper.selectById(userId);
        if (existUser == null) {
            return ResultVO.error("用户不存在");
        }
        
        user.setId(userId);
        // 不允许修改用户名和密码
        user.setUsername(null);
        user.setPassword(null);
        
        userMapper.updateById(user);
        
        return getUserInfo(userId);
    }
    
    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }
    
    private UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
