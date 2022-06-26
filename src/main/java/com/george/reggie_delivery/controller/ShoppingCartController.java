package com.george.reggie_delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.george.reggie_delivery.common.R;
import com.george.reggie_delivery.common.ThreadLocalUtils;
import com.george.reggie_delivery.entity.ShoppingCart;
import com.george.reggie_delivery.service.ShoppingCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author George
 * @Date 2022-06-25-9:28
 * @Description TODO
 * @Version 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCarService shoppingCarService;

    @PostMapping("/add")
    public R<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        Long userId = ThreadLocalUtils.getValue();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart shoppingCarResult = shoppingCarService.getOne(shoppingCartLambdaQueryWrapper);
        if(shoppingCarResult == null){
            shoppingCarResult = shoppingCart;
            shoppingCarResult.setCreateTime(new Date());
            shoppingCarResult.setNumber(1);
            shoppingCarService.save(shoppingCarResult);
        }else{
            shoppingCarResult.setNumber(shoppingCarResult.getNumber()+1);
            shoppingCarService.updateById(shoppingCarResult);
        }
        return R.success(shoppingCarResult);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> subShoppingCart(@RequestBody ShoppingCart shoppingCart){
        Long userId = ThreadLocalUtils.getValue();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart shoppingCarResult = shoppingCarService.getOne(shoppingCartLambdaQueryWrapper);

        shoppingCarResult.setNumber(shoppingCarResult.getNumber()-1);
        shoppingCarService.updateById(shoppingCarResult);
        if(shoppingCarResult.getNumber()==0){
            shoppingCarService.removeById(shoppingCarResult.getId());
        }

        return R.success(shoppingCarResult);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> listShoppingCart(){
        Long userId = ThreadLocalUtils.getValue();
        LambdaQueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCarService.list(shoppingCartQueryWrapper);
        return R.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    public R<String> cleanShoppingCart(){
        Long userId = ThreadLocalUtils.getValue();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCarService.remove(shoppingCartLambdaQueryWrapper);
        return R.success("清除购物车成功");
    }
}
