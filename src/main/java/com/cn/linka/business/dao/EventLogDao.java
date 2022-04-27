package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: linka
 * @description: 事件类型目前分为点击和曝光
 * @author: wangding
 * @create: 2022-04-27 15:37
 */
@Data
@ApiModel(value="EventLogDao",description="事件记录请求Dao")
public class EventLogDao implements Serializable {
    @ApiModelProperty("事件类型")
    private String eventType;
    @ApiModelProperty("事件信息")
    private String eventDetail;

}