package com.db.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @description: 根据不同的渠道id(支付方式) 返回不同的支付提交表单
 */
@Api(tags = "生成支付表单服务")
public interface PayContextService {
	/**
	 * 根据部队支付渠道生成不同的from
	 * @param channelId 支付渠道表的渠道id 如：yinlian_pay，ali_pay
	 * @param payToken 保存支付id信息的令牌
	 * @return 表单HTML
	 */
	@ApiOperation("支付表单接口")
	@GetMapping("/toPayHtml")
	public BaseResponse<JSONObject> toPayHtml(@RequestParam("channelId") String channelId,
											  @RequestParam("payToken") String payToken);

}
