package com.cn.linka.business.dao;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class UserEmailRegisterRequest {
    @Email(message = "邮箱格式异常")
    private String email;
    @Length(message = "验证码长度为6位", max = 6)
    private String verifyCode;
    @Length(message = "密码最小6位，最大为20位", max = 20, min = 6)
    private String password;
}
