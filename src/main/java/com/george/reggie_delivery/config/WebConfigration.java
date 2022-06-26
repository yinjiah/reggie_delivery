package com.george.reggie_delivery.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author George
 * @Date 2022-06-01-16:48
 * @Description TODO
 * @Version 1.0
 */
@Configuration
public class WebConfigration extends WebMvcConfigurationSupport{

    /**
     * @author George
     * @description //TODO 进行静态路径与文件夹的映射
     * @date 17:15 2022/6/1 
     * @param registry FDF
     * @return void
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * @author George
     * @date 14:25 2022/6/15
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @description 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

}
