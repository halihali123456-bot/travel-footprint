package com.travel.footprint.config;

import com.travel.footprint.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BindException.class)
    public ResultVO<Void> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getDefaultMessage()).append("; ");
        }
        return ResultVO.error(sb.toString());
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResultVO<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: ", e);
        return ResultVO.error(e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResultVO<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return ResultVO.error("系统繁忙，请稍后再试");
    }
}
