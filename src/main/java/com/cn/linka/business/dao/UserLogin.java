package com.cn.linka.business.dao;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Builder
@Data
public class UserLogin {
    private String userId;
    private String userName;
    private String email;
    private String phone;
    private String userStatus;
    private Date createDt;
    private Date updateDt;
    private String token;
}
