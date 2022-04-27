package com.cn.linka.business.controller;

import com.cn.linka.business.dao.EventLogDao;
import com.cn.linka.business.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 事件记录
 */
@RestController
@Api(value = "事件记录controller", tags = {"用户事件记录接口"})
public class EventLogController {

    @Autowired
    private EventService eventService;

    @PostMapping(value = "/event-record")
    @ApiOperation("用户事件记录")
    public void eventRecord(@ApiParam(value = "事件记录请求", required = true) @RequestBody EventLogDao eventLogDao) {
        eventService.insert(eventLogDao);
    }
}