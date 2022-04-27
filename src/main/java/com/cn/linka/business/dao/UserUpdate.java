package com.cn.linka.business.dao;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class UserUpdate {
    private String userId;
    private String userName;
    private String userImg;

}
