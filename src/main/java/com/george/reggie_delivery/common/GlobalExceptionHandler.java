package com.george.reggie_delivery.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author George
 * @Date 2022-06-14-12:00
 * @Description TODO
 * @Version 1.0
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> sqlExceptionHandler(SQLIntegrityConstraintViolationException exp){
        //log.info("sqlException handler:{}",exp.getMessage());
        if(exp.getMessage().contains("Duplicate entry")){
            String[] split = exp.getMessage().split(" ");
            return R.error(split[2]+"已存在！");

        }
        return R.error("未知错误！");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }
}
