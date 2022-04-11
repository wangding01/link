//package com.cn.linka.common.config;
//
//import com.cn.linka.common.jwt.JWTInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * @author dan
// */
//@Configuration
//public class InterceptorConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new JWTInterceptor())
//                //拦截的路径
//                .addPathPatterns("/**")
//                //排除登录接口
//                .excludePathPatterns("/user/login");
//    }
//}
