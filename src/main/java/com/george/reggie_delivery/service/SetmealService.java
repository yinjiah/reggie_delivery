package com.george.reggie_delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.george.reggie_delivery.dto.SetmealDto;
import com.george.reggie_delivery.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    void saveSetmealDish(SetmealDto setmealDto);
}
