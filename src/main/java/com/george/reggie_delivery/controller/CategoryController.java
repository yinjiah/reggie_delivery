package com.george.reggie_delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.george.reggie_delivery.common.R;
import com.george.reggie_delivery.entity.Category;
import com.george.reggie_delivery.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author George
 * @Date 2022-06-19-10:53
 * @Description TODO
 * @Version 1.0
 */
@RestController
@RequestMapping("/category")
@ResponseBody
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @author George
     * @date 11:19 2022/6/20
     * @param category
     * @return com.george.reggie_delivery.common.R<java.lang.String>
     * @description 新增分类
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        if(category != null){
            categoryService.save(category);
        }
        return R.success("新增分类成功");
    }

    /**
     * @author George
     * @date 11:20 2022/6/20
     * @param pageSize
     * @param page
     * @return com.george.reggie_delivery.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.george.reggie_delivery.entity.Category>>
     * @description 分页展示种类内容
     */
    @GetMapping("/page")
    public R<Page<Category>> getCategoryPageInfo(Long pageSize,Long page){
        Page<Category> categoryPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        //lambdaQueryWrapper.like(StringUtils.isNotBlank())
        categoryService.page(categoryPage,lambdaQueryWrapper);
        return R.success(categoryPage);
    }
    
    /**
     * @author George
     * @date 11:20 2022/6/20
     * @param ids
     * @return com.george.reggie_delivery.common.R<java.lang.String>
     * @description 删除空种类
     */
    @DeleteMapping
    public R<String> deleteCategory(@RequestParam("ids") Long ids){
        categoryService.deleteEmptyById(ids);
        return R.success("删除成功");
    }

    /**
     * @author George
     * @date 11:26 2022/6/20
     * @param category
     * @return com.george.reggie_delivery.common.R<java.lang.String>
     * @description 修改分类信息
     */
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    @GetMapping("/list")
    public R<List<Category>> listCategory(Category category){
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(list);
    }
}
