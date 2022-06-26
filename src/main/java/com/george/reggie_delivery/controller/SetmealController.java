package com.george.reggie_delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.george.reggie_delivery.common.CustomException;
import com.george.reggie_delivery.common.R;
import com.george.reggie_delivery.dto.SetmealDto;
import com.george.reggie_delivery.entity.Category;
import com.george.reggie_delivery.entity.Setmeal;
import com.george.reggie_delivery.entity.SetmealDish;
import com.george.reggie_delivery.service.CategoryService;
import com.george.reggie_delivery.service.SetmealDishService;
import com.george.reggie_delivery.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author George
 * @Date 2022-06-21-14:58
 * @Description TODO
 * @Version 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmealDish(setmealDto);
        return R.success("套餐新增成功");
    }

    @GetMapping("/page")
    public R<Page> pageInfo(int page, int pageSize, String name){
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        setmealService.page(setmealPage, setmealLambdaQueryWrapper);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList = records.stream().map(item -> {
            Long categoryId = item.getCategoryId();
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status,@RequestParam("ids") List<Long> ids){
        LambdaUpdateWrapper<Setmeal> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        dishLambdaUpdateWrapper.in(Setmeal::getId,ids)
                .set(Setmeal::getStatus,status);
        setmealService.update(dishLambdaUpdateWrapper);
        return R.success("状态更新成功");
    }


    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    public R<String> deleteSetmeal(@RequestParam("ids") List<Long> ids){
        LambdaQueryWrapper<Setmeal> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Setmeal::getId,ids)
                .eq(Setmeal::getStatus,1);
        long count = setmealService.count(dishLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("存在套餐仍在销售中，无法删除");
        }
        setmealService.removeBatchByIds(ids);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
        return R.success("删除菜品成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(setmeal!=null,Setmeal::getCategoryId,setmeal.getCategoryId())
                .eq(setmeal!=null,Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        return R.success(list);
    }

}
