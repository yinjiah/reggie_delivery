package com.george.reggie_delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.george.reggie_delivery.common.CustomException;
import com.george.reggie_delivery.entity.Category;
import com.george.reggie_delivery.entity.Dish;
import com.george.reggie_delivery.entity.Setmeal;
import com.george.reggie_delivery.mapper.CategoryMapper;
import com.george.reggie_delivery.mapper.DishMapper;
import com.george.reggie_delivery.mapper.SetmealMapper;
import com.george.reggie_delivery.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author George
 * @Date 2022-06-01-13:24
 * @Description TODO
 * @Version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * @author George
     * @date 11:08 2022/6/20
     * @param id
     * @return int
     * @description 删除空种类
     */
    @Override
    public int deleteEmptyById(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        Long dishCount = dishMapper.selectCount(dishLambdaQueryWrapper);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        Long setMealCount = setmealMapper.selectCount(setmealLambdaQueryWrapper);
        if(dishCount>0){
            throw new CustomException("该分类中拥有其他菜品");
        }
        if(setMealCount>0){
            throw new CustomException("该分类中有其他套餐");
        }
        return categoryMapper.deleteById(id);

    }
}
