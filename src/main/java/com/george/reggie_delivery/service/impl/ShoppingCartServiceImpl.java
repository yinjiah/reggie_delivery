package com.george.reggie_delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.george.reggie_delivery.entity.ShoppingCart;
import com.george.reggie_delivery.mapper.ShoppingCartMapper;
import com.george.reggie_delivery.service.ShoppingCarService;
import org.springframework.stereotype.Service;

/**
 * @Author George
 * @Date 2022-06-01-13:24
 * @Description TODO
 * @Version 1.0
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements ShoppingCarService {

}
