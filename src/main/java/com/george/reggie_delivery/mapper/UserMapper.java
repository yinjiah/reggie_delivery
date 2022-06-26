package com.george.reggie_delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.george.reggie_delivery.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
