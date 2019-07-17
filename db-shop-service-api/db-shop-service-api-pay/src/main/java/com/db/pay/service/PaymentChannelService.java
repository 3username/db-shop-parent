package com.db.pay.service;

import com.db.pay.output.dto.PaymentChannelDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Api(tags = "查询所有支付渠道接口服务")
public interface PaymentChannelService {
    /** 查询所有支付渠道 */
    @ApiOperation("查询所有支付渠道接口")
    @GetMapping("/selectAll")
    public List<PaymentChannelDTO> selectAll();
}