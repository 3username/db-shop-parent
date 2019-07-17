package com.db.seckill.service;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 秒杀商品服务
 */
@Api(tags = "秒杀服务")
public interface SeckillCommodityService {

    /**
     * 用户秒杀接口 phone和userid都可以的
     * @param phone 手机号码
     * @param seckillId 库存id
     * @return 秒杀结果
     */
    @ApiOperation("用户秒杀接口")
    @GetMapping("/seckill")
    public BaseResponse<JSONObject> seckill(String phone,Long seckillId);

    /**
     * 新增对应商品库存令牌桶
     * @param tokenQuantity 库存数量
     * @seckillId 商品库存id
     */
    @RequestMapping("/addSpikeToken")
    public BaseResponse<JSONObject> addSpikeToken(Long seckillId, Long tokenQuantity);
}
