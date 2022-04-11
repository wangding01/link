package com.cn.linka.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-11 16:51
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${link.fileStorePath}")
    private String filePath;

    /**
     * springboot 无法直接访问静态资源，需要放开资源访问路径。
     * 添加静态资源文件，外部可以直接访问地址
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 意思是我们通过项目访问资源路径为/localPath开头的将会被映射到D:/Temp下
        registry.addResourceHandler("/linkPath/**").addResourceLocations("file:"+filePath);
    }
}