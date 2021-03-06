package com.cn.linka.business.dao;

import lombok.Builder;
import lombok.Data;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-11 16:12
 */
@Data
@Builder
public class FileUploadDao {
    private String cosBaseUrl;
    private String loadUrl;
}