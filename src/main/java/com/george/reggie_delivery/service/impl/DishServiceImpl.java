package com.george.reggie_delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.george.reggie_delivery.dto.DishDto;
import com.george.reggie_delivery.entity.Dish;
import com.george.reggie_delivery.entity.DishFlavor;
import com.george.reggie_delivery.mapper.DishMapper;
import com.george.reggie_delivery.service.DishFlavorService;
import com.george.reggie_delivery.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author George
 * @Date 2022-06-01-13:24
 * @Description TODO
 * @Version 1.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService{

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * @author George
     * @date 18:48 2022/6/20
     * @param dishDto
     * @return void
     * @description 保存产品和口味
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithFlavor(DishDto dishDto) {
        dishMapper.insert(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(item->{item.setDishId(dishId);});
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getDishInfoById(Long id) {
        Dish dish = dishMapper.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithFlavors(DishDto dishDto) {
        dishMapper.updateById(dishDto);

        Long dishId = dishDto.getId();

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> flavorsList = flavors.stream().map(item -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavorsList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(List<Long> id) {
        dishMapper.deleteBatchIds(id);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId,id);
        return dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
    }
}
