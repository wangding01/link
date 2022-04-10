package com.cn.linka.business.dao;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String userStatus;
    private Date createDt;
    private Date updateDt;
}
