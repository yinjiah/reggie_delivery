package com.george.reggie_delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.george.reggie_delivery.common.R;
import com.george.reggie_delivery.entity.User;
import com.george.reggie_delivery.service.UserService;
import com.george.reggie_delivery.util.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author George
 * @Date 2022-06-22-17:39
 * @Description TODO
 * @Version 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private SMSUtils smsUtils;

    @PostMapping("/login")
    public R<User> login(@RequestBody User user, HttpServletRequest request){
        if(user != null){
            String phone = user.getPhone();
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(phone!=null ,User::getPhone,phone);
            User selectUser = userService.getOne(userLambdaQueryWrapper);
            if(selectUser == null){
                userService.save(user);
                log.info(user.getId().toString());
                request.getSession().setAttribute("user",user.getId());
            } else{
                request.getSession().setAttribute("user",selectUser.getId());
            }

        }
        return R.success(user);
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return  R.success("退出成功");
    }
    //@PostMapping("/sendMsg")
    //public R<String> sendMsg(User user, HttpSession httpSession){
    //    Integer code = ValidateCodeUtils.generateValidateCode(6);
    //    httpSession.setAttribute("code", code);
    //    String email = user.getPhone();
    //    if(StringUtils.isNotBlank(email)){
    //        smsUtils.sendMail(email,code);
    //        return R.success("验证法已发送成功！");
    //    }
    //    return R.error("验证码发送失败");
    //}
}
