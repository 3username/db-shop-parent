package com.db.service;

import com.db.base.BaseResponse;
import com.db.entity.AppEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "微信接口")
public interface AppService {

    /** 获取app应用信息*/
    @GetMapping("/getapp")
    @ApiOperation(value = "微信应用服务接口")
    public BaseResponse<AppEntity> getApp();
}
