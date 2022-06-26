package com.george.reggie_delivery.filter;

import com.alibaba.fastjson.JSON;
import com.george.reggie_delivery.common.R;
import com.george.reggie_delivery.common.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author George
 * @Date 2022-06-13-21:13
 * @Description TODO
 * @Version 1.0
 */
@WebFilter(filterName = "loginFilter",urlPatterns = "/*")
@Slf4j
public class LoginFilter implements Filter{

    //路径匹配器 能够匹配通配符
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * @author George
     * @date 21:59 2022/6/13
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @return void
     * @description 登录过滤器
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        String[] filtedUrls = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"

        };
        boolean match = checkUri(filtedUrls, requestURI);
        if(match){
            filterChain.doFilter(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Long employeeId = (Long) session.getAttribute("employee");
        if(employeeId != null){
            ThreadLocalUtils.setValue(employeeId);
            filterChain.doFilter(request, response);
            return ;
        }

        Long userId = (Long) session.getAttribute("user");
        if(userId != null){
            ThreadLocalUtils.setValue(userId);
            filterChain.doFilter(request, response);
            return ;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    /**
     * @author George
     * @date 21:58 2022/6/13
     * @param filtedUrls 不需要处理的Url数组
     * @param requestURI 请求的Url
     * @return boolean
     * @description 检查路径是否匹配
     */
    private boolean checkUri(String[] filtedUrls, String requestURI) {
        for (String filtedUrl : filtedUrls) {
            boolean match = PATH_MATCHER.match(filtedUrl, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
