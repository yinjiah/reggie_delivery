package com.george.reggie_delivery.dto;

import com.george.reggie_delivery.entity.Dish;
import com.george.reggie_delivery.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
