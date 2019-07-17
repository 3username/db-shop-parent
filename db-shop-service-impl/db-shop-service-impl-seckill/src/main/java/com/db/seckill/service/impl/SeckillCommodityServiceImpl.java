package com.db.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.core.token.GenerateToken;
import com.db.seckill.mapper.SeckillMapper;
import com.db.seckill.mapper.entity.SeckillEntity;
import com.db.seckill.mq.producer.SeckillCommodityProducer;
import com.db.seckill.service.SeckillCommodityService;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SeckillCommodityServiceImpl extends BaseApiService<JsonObject> implements SeckillCommodityService {

    //private final SeckillMapper seckillMapper;
    //private final OrderMapper orderMapper;
    //private final RedisUtil redisUtil;
    private final SeckillMapper seckillMapper;
    private final GenerateToken generateToken;
    private final SeckillCommodityProducer seckillCommodityProducer;

    @Autowired
    public SeckillCommodityServiceImpl(SeckillMapper seckillMapper, GenerateToken generateToken, SeckillCommodityProducer seckillCommodityProducer) {

        this.seckillMapper = seckillMapper;
        this.generateToken = generateToken;
        this.seckillCommodityProducer = seckillCommodityProducer;
    }

    @Override
    @Transactional
    public BaseResponse<JSONObject> seckill(String phone, Long seckillId) {
        //验证参数
        if(StringUtils.isEmpty(phone)){
            return setResultError("手机号码不能为空");
        }
        if(seckillId == null){
            return setResultError("库存id不能为空");
        }

        // 2.从redis从获取对应的秒杀token
        String seckillToken = generateToken.getListKeyToken(seckillId + "");
        if(StringUtils.isEmpty(seckillToken)){
            log.info(">>>seckillId：{},该秒杀已售空，请下次再来！", seckillId);
            return setResultError("亲，该秒杀已经售空，请下次再来!");
        }
        // 3.获取到秒杀token之后，异步放入mq中实现修改商品的库存
        sendSeckillMsg(seckillId, phone);
        return setResultSuccess("正在排队中.......");
    }


    /**
     * 获取到秒杀token之后，异步放入mq中实现修改商品的库存
     */
    @Async
    public void sendSeckillMsg(Long seckillId, String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seckillId", seckillId);
        jsonObject.put("phone",phone);
        seckillCommodityProducer.send(jsonObject);
    }

    // 采用redis数据库类型为 list类型 key为 商品库存id list 多个秒杀token
    @Override
    public BaseResponse<JSONObject> addSpikeToken(Long seckillId, Long tokenQuantity) {
        // 1.验证参数
        if (seckillId == null) {
            return setResultError("商品库存id不能为空!");
        }
        if (tokenQuantity == null) {
            return setResultError("token数量不能为空!");
        }
        SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity == null) {
            return setResultError("商品信息不存在!");
        }
        // 2.使用多线程异步生产令牌
        createSeckillToken(seckillId, tokenQuantity);
        return setResultSuccess("令牌正在生成中.....");
    }

    @Async
    public void createSeckillToken(Long seckillId, Long tokenQuantity) {
        generateToken.createListToken("seckill_", seckillId + "", tokenQuantity);
    }

    /*@Override
    @Transactional
    public BaseResponse<JSONObject> seckill(String phone, Long seckillId) {
        //验证参数
        if(StringUtils.isEmpty(phone)){
            return setResultError("手机号码不能为空");
        }
        if(seckillId == null){
            return setResultError("库存id不能为空");
        }

        SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity == null) {
            return setResultError("商品信息不存在!");
        }
        //用户限制频率
        Long timeout = 10l;
        Boolean resultNx = redisUtil.setNx(phone, seckillId + "", timeout);
        if(!resultNx){
            return setResultError("访问次数频繁，请"+timeout+"秒后重试");
        }
        //修改数据库对应的库存 1万中只有100个抢购成功 提前生成好100个token 谁能够抢购成功token放入到mq中实现异步修改库存
        Long version = seckillEntity.getVersion();
        int inventoryDeduction = seckillMapper.inventoryDeduction(seckillId,version);
        if (!toDaoResult(inventoryDeduction)) {
            log.info(">>>修改库存失败>>>>inventoryDeduction返回为{} 秒杀失败！", inventoryDeduction);
            return setResultError("亲，请稍后重试!");
        }
        // 4.添加秒杀成功订单 基于MQ实现异步形式
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserPhone(phone);
        orderEntity.setSeckillId(seckillId);
        int insertOrder = orderMapper.insertOrder(orderEntity);
        if (!toDaoResult(insertOrder)) {
            return setResultError("亲，请稍后重试!");
        }
        log.info(">>>修改库存成功>>>>inventoryDeduction返回为{} 秒杀成功", inventoryDeduction);
        return setResultSuccess("恭喜您，秒杀成功!");
    }*/
}
