package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Builder
@Data
@ApiModel(value="UserLogin",description="登陆返回值")
public class UserLogin {
    private String userId;
    private String userName;
    private String email;
    private String phone;
    private String userStatus;
    private String userImg;
    private Date createDt;
    private Date updateDt;
    private String token;
}
