package com.cn.linka.business.dao;

import lombok.Builder;
import lombok.Data;
import java.util.Date;
@Builder
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

    public static UserLogin toUserLogin(User user, String token) {
        return UserLogin.builder().email(user.getEmail())
                .phone(user.getPhone())
                .userName(user.getUserName())
                .userId(user.getUserId())
                .userStatus(user.getUserStatus())
                .createDt(user.getCreateDt())
                .updateDt(user.getCreateDt())
                .token(token)
                .build();
    }
}
