package com.cn.linka.business.dao;

import lombok.Data;

import java.io.Serializable;
@Data
public class GetPortalByIndexRequest implements Serializable {
    private String index;
}
