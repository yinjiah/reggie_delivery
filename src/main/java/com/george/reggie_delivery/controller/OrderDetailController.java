package com.george.reggie_delivery.controller;

import com.george.reggie_delivery.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author George
 * @Date 2022-06-21-14:58
 * @Description TODO
 * @Version 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;


}
