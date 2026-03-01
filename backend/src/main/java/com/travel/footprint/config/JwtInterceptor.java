package com.travel.footprint.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.travel.footprint.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token
        String token = request.getHeader("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 401);
            result.put("message", "请先登录");
            
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(result));
            return false;
        }
        
        token = token.substring(7);
        
        try {
            DecodedJWT jwt = jwtUtils.verifyToken(token);
            
            if (jwtUtils.isTokenExpired(token)) {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                
                Map<String, Object> result = new HashMap<>();
                result.put("code", 401);
                result.put("message", "登录已过期，请重新登录");
                
                ObjectMapper mapper = new ObjectMapper();
                response.getWriter().write(mapper.writeValueAsString(result));
                return false;
            }
            
            // 将用户信息存入request
            Long userId = jwtUtils.getUserIdFromToken(token);
            String username = jwtUtils.getUsernameFromToken(token);
            
            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 401);
            result.put("message", "登录已过期，请重新登录");
            
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(result));
            return false;
        }
    }
}
