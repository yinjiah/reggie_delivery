package com.george.reggie_delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.george.reggie_delivery.entity.Employee;
import com.george.reggie_delivery.mapper.EmployeeMapper;
import com.george.reggie_delivery.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Author George
 * @Date 2022-06-01-13:24
 * @Description TODO
 * @Version 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{

}
