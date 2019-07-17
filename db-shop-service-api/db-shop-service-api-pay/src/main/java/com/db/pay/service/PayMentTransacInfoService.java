package com.db.pay.service;

import com.db.base.BaseResponse;
import com.db.pay.output.dto.PayMentTransacDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 根据token查找支付信息
 */
@Api(tags = "查找支付信息服务")
public interface PayMentTransacInfoService {
    /**根据token查找支付信息*/
    @ApiOperation("根据token查找支付信息接口")
    @GetMapping("/tokenByPayMentTransac")
    public BaseResponse<PayMentTransacDTO> tokenByPayMentTransac(@RequestParam("token") String token);
}
