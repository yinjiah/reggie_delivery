package com.george.reggie_delivery.controller;

import com.george.reggie_delivery.common.R;
import com.george.reggie_delivery.entity.Orders;
import com.george.reggie_delivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author George
 * @Date 2022-06-21-14:58
 * @Description TODO
 * @Version 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("结算成功");
    }
}
