package com.db.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseResponse;
import com.db.pay.input.dto.PayCratePayTokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 创建支付信息接口
 */
@Api(tags = "创建支付token接口服务")
public interface PayMentTransacTokenService {

    /**
     * 创建支付信息，返回令牌
     * @param payCratePayTokenDto 支付信息
     * @return token
     */
    @GetMapping("/cratePayToken")
    @ApiOperation(value = "创建token接口")
    public BaseResponse<JSONObject> cratePayToken(PayCratePayTokenDto payCratePayTokenDto);
}
