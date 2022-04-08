package com.cn.linka;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.cn.linka.business.mapper")
@SpringBootApplication
public class LinkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkaApplication.class, args);
    }

}
