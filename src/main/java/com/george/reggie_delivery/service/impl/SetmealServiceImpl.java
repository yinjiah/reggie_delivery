package com.george.reggie_delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.george.reggie_delivery.dto.SetmealDto;
import com.george.reggie_delivery.entity.Setmeal;
import com.george.reggie_delivery.entity.SetmealDish;
import com.george.reggie_delivery.mapper.SetmealDishMapper;
import com.george.reggie_delivery.mapper.SetmealMapper;
import com.george.reggie_delivery.service.SetmealDishService;
import com.george.reggie_delivery.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author George
 * @Date 2022-06-01-13:24
 * @Description TODO
 * @Version 1.0
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService{
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void saveSetmealDish(SetmealDto setmealDto) {
        setmealMapper.insert(setmealDto);
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> setmealDishList = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }
}
