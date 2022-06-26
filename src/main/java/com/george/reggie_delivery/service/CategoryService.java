package com.george.reggie_delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.george.reggie_delivery.entity.Category;

public interface CategoryService extends IService<Category> {

    int deleteEmptyById(Long id);
}
