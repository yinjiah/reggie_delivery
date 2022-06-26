package com.george.reggie_delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.george.reggie_delivery.common.CustomException;
import com.george.reggie_delivery.common.R;
import com.george.reggie_delivery.dto.DishDto;
import com.george.reggie_delivery.entity.Category;
import com.george.reggie_delivery.entity.Dish;
import com.george.reggie_delivery.entity.DishFlavor;
import com.george.reggie_delivery.service.CategoryService;
import com.george.reggie_delivery.service.DishFlavorService;
import com.george.reggie_delivery.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author George
 * @Date 2022-06-20-18:30
 * @Description TODO
 * @Version 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        log.info(dishDto.toString());
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> pageInfo(int page,int pageSize, String name){
        Page<Dish> dishPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name!=null,Dish::getName,name)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,dishLambdaQueryWrapper);
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> dishRecords = dishPage.getRecords();
        List<DishDto> list =  dishRecords.stream().map(item -> {
            Long categoryId = item.getCategoryId();
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryLambdaQueryWrapper.eq(Category::getId,categoryId);
            Category category = categoryService.getOne(categoryLambdaQueryWrapper);
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            if(category != null){
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getDishInfo(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getDishInfoById(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto){
        dishService.updateWithFlavors(dishDto);
        return R.success("修改菜品成功");
    }

    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> ids){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId,ids)
                .eq(Dish::getStatus,1);
        long count = dishService.count(dishLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("存在菜品仍在销售中，无法删除");
        }
        dishService.deleteById(ids);
        return R.success("删除菜品成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status,@RequestParam("ids") List<Long> ids){
        LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        dishLambdaUpdateWrapper.in(Dish::getId,ids)
                .set(Dish::getStatus,status);
        dishService.update(dishLambdaUpdateWrapper);
        return R.success("状态更新成功");
    }

    //@GetMapping("/list")
    //public R<List<Dish>> listDishByCategory( Dish dish){
    //    Long categoryId = dish.getCategoryId();
    //    LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
    //    dishLambdaQueryWrapper.eq(categoryId != null,Dish::getCategoryId,categoryId)
    //            .eq(Dish::getStatus,1)
    //            .orderByAsc(Dish::getSort)
    //            .orderByDesc(Dish::getUpdateTime);
    //    List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);
    //    return R.success(dishList);
    //}
    @GetMapping("/list")
    public R<List<DishDto>> listDishByCategory( Dish dish){
        Long categoryId = dish.getCategoryId();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(categoryId != null,Dish::getCategoryId,categoryId)
                .eq(Dish::getStatus,1)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);
        List<DishDto> dishDtoList = dishList.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            log.info("{}",flavorList.size());
            dishDto.setFlavors(flavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }


}
