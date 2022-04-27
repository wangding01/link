package com.cn.linka.business.dao;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserUpdatePasswordDao {
    private String userId;
    private String email;
    private String verifyCode;
    private String password;

}
