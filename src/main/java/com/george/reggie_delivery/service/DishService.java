package com.george.reggie_delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.george.reggie_delivery.dto.DishDto;
import com.george.reggie_delivery.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto getDishInfoById(Long id);

    void updateWithFlavors(DishDto dishDto);

    boolean deleteById(List<Long> id);
}
