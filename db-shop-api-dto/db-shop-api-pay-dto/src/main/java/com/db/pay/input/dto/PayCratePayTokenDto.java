package com.db.pay.input.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "生成支付信息类")
public class PayCratePayTokenDto {
    /** 支付金额*/
    @ApiModelProperty(value = "支付金额")
    @NotNull(message = "支付金额不能为空")
    private Long payAmount;
    /** 订单号码*/
    @ApiModelProperty(value = "订单ID")
    @NotNull(message = "订单号码不能为空")
    private String orderId;
    /**userId */
    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "userId不能空")
    private Long userId;

}
