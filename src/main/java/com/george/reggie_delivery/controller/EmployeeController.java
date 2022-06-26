package com.george.reggie_delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.george.reggie_delivery.common.R;
import com.george.reggie_delivery.entity.Employee;
import com.george.reggie_delivery.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author George
 * @Date 2022-06-01-18:28
 * @Description TODO
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/employee")
@ResponseBody
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

     /**
      * @author George
      * @date 14:36 2022/6/12
      * @param request http请求
      * @param employee 员工
      * @return com.george.reggie_delivery.common.R<com.george.reggie_delivery.entity.Employee>
      * @description 登录功能
      */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee user = employeeService.getOne(lambdaQueryWrapper);
        if(user == null || !user.getPassword().equals(md5Password)){
            return R.error("用户名或密码输入错误！");
        } else if(user.getStatus() == 0){
            return R.error("该用户已被禁用");
        }
        HttpSession session = request.getSession();
        session.setAttribute("employee",user.getId());
        return R.success(user);
    }
    /**
     * @author George
     * @date 11:13 2022/6/14
     * @param request
     * @return com.george.reggie_delivery.common.R<java.lang.String>
     * @description 登出
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        return  R.success("退出成功");
    }

    /**
     * @author George
     * @date 11:14 2022/6/14
     * @param employee
     * @return com.george.reggie_delivery.common.R<java.lang.String>
     * @description 新增员工
     * @requstbody 接受请求体中的数据，一般为json字符串
     */
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.success("新增用户成功");
    }
    /**
     * @author George
     * @date 9:04 2022/6/19
     * @param page
     * @param pageSize
     * @param name
     * @return com.george.reggie_delivery.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.george.reggie_delivery.entity.Employee>>
     * @description 分页查询员工信息
     */
    @GetMapping("/page")
    public R<Page<Employee>> getPageEmployeeInfo(int page, int pageSize, String name){
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(name),Employee::getUsername,name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }
    
    /**
     * @author George
     * @date 9:05 2022/6/19
     * @param request
     * @param employee
     * @return com.george.reggie_delivery.common.R<java.lang.String>
     * @description 更新员工信息
     */
    @PutMapping
    public R<String> updateInfo(HttpServletRequest request,@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("更新成功");
    }
    
    /**
     * @author George
     * @date 9:05 2022/6/19
     * @param id
     * @return com.george.reggie_delivery.common.R<com.george.reggie_delivery.entity.Employee>
     * @description 编辑员工信息回显
     */
    @GetMapping("/{id}")
    public R<Employee> showInfoById(@PathVariable("id") Long id){
        Employee emp = employeeService.getById(id);
        if(emp != null){
            return R.success(emp);
        }
        return R.error("没有查到该用户");
    }
}
