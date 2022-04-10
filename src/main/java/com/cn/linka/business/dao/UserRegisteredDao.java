package com.cn.linka.business.dao;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册成功返回值
 */
@Data
public class UserRegisteredDao implements Serializable {
    String userId;
}
