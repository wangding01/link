package com.cn.linka.common.config;

import com.cn.linka.common.jwt.JWTInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 意思是我们通过项目访问资源路径为/localPath开头的将会被映射到D:/Temp下
        registry.addResourceHandler("/linkPath/**").addResourceLocations("file:" + filePath);
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/thymeleaf/**")
                .addResourceLocations("classpath:/thymeleaf/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor())
                //拦截的路径
                .addPathPatterns("/**")
                //排除登录接口
                .excludePathPatterns("/getUser",
                        "/check-email",
                        "/check-email-verifyCode",
                        "/user-email-login",
                        "/get-email-verify-code",
                        "/check-email",
                        "/registered-email",
                        "/get-portal-by-index",
                        "/user/theme/get-theme-by-id",
                        "/user-wx-login",
                        "/user-verifyCode-login")
                .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/v3/**", "/swagger-ui.html/**")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/download-by-cos/*")
                .excludePathPatterns("/event-record")
                .excludePathPatterns("/link-test/**")
                .excludePathPatterns("/thymeleaf/**")
                .excludePathPatterns("/wxPay/**")//上线删除
                .excludePathPatterns("/api/ucenter/wx/**")//上线删除
                .excludePathPatterns("/linkPath/*");
    }
    /**
     * 跨域支持
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD")
                .maxAge(3600 * 24);
    }

}