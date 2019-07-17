package com.db.seckill.service;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 查询秒杀记录
 *
 */
@Api(tags = "查询秒杀结果")
public interface OrderSeckillService {

	@ApiOperation("查询秒杀结果接口")
	@RequestMapping("/getOrder")
	public BaseResponse<JSONObject> getOrder(String phone, Long seckillId);

}
