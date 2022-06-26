package com.george.reggie_delivery.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author George
 * @Date 2022-06-19-10:12
 * @Description TODO
 * @Version 1.0
 */
@Component
public class EmployeeMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date updateTime = new Date();
        this.strictInsertFill(metaObject,"createTime", Date.class,updateTime);
        this.strictInsertFill(metaObject,"updateTime", Date.class,updateTime);
        this.strictInsertFill(metaObject,"createUser",Long.class,ThreadLocalUtils.getValue());
        this.strictInsertFill(metaObject,"updateUser",Long.class,ThreadLocalUtils.getValue());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateUser",Long.class,ThreadLocalUtils.getValue());
        this.strictUpdateFill(metaObject,"updateTime",Date.class,new Date());
    }
}
