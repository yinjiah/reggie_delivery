package com.george.reggie_delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.george.reggie_delivery.entity.Orders;

public interface OrderService extends IService<Orders>{
    void submit(Orders orders);
}
