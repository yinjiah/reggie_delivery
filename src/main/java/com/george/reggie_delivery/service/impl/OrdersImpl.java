package com.george.reggie_delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.george.reggie_delivery.common.CustomException;
import com.george.reggie_delivery.common.ThreadLocalUtils;
import com.george.reggie_delivery.entity.*;
import com.george.reggie_delivery.mapper.OrderMapper;
import com.george.reggie_delivery.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author George
 * @Date 2022-06-01-13:24
 * @Description TODO
 * @Version 1.0
 */
@Service
public class OrdersImpl extends ServiceImpl<OrderMapper,Orders> implements OrderService {

     @Autowired
     private AddressBookService addressBookService;

     @Autowired
     private OrderDetailService orderDetailService;
     @Autowired
     private UserService userService;
     @Autowired
     private ShoppingCarService shoppingCarService;

     @Override
     public void submit(Orders orders) {
         //获取用户id
         Long userId = ThreadLocalUtils.getValue();
         Long addressBookId = orders.getAddressBookId();
         AddressBook addressBook = addressBookService.getById(addressBookId);

         //查询购物车中的各个款项
         LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
         shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
         List<ShoppingCart> shoppingCartList = shoppingCarService.list(shoppingCartLambdaQueryWrapper);
         if(shoppingCartList == null || shoppingCartList.size() == 0){
             throw new CustomException("购物车中还没有物品，赶快去选购物品吧！");
         }
         User user = userService.getById(userId);
         if(addressBook==null){
             throw new CustomException("订单地址错误，无法结算订单");
         }
         AtomicInteger amount = new AtomicInteger(0);
         Long orderId = IdWorker.getId();
         List<OrderDetail> orderDetailList = shoppingCartList.stream().map(item -> {
             OrderDetail orderDetail = new OrderDetail();
             orderDetail.setOrderId(orderId);
             orderDetail.setDishId(item.getDishId());
             orderDetail.setImage(item.getImage());
             orderDetail.setAmount(item.getAmount());
             orderDetail.setDishFlavor(item.getDishFlavor());
             orderDetail.setNumber(item.getNumber());
             orderDetail.setSetmealId(item.getSetmealId());
             orderDetail.setName(item.getName());
             amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
             return orderDetail;
         }).collect(Collectors.toList());
         orders.setId(orderId);
         orders.setUserId(userId);
         orders.setUserName(user.getName());
         orders.setNumber(String.valueOf(orderId));
         orders.setStatus(2);
         orders.setOrderTime(new Date());
         orders.setCheckoutTime(new Date());
         orders.setConsignee(addressBook.getConsignee());
         orders.setPhone(addressBook.getPhone());
         orders.setAmount(new BigDecimal(amount.get()));
         orders.setAddress((addressBook.getProvinceName()==null?"":addressBook.getProvinceName())
                 + (addressBook.getCityName()==null?"":addressBook.getCityName())
                 + (addressBook.getDistrictName()==null?"":addressBook.getProvinceName())
                 + (addressBook.getDetail()==null?"":addressBook.getDetail()));
         this.save(orders);

         orderDetailService.saveBatch(orderDetailList);
         shoppingCarService.remove(shoppingCartLambdaQueryWrapper);

     }
 }
