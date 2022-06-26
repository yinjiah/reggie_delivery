package com.george.reggie_delivery.dto;

import com.george.reggie_delivery.entity.Setmeal;
import com.george.reggie_delivery.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
