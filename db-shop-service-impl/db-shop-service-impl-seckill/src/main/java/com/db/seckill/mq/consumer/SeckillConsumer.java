package com.db.seckill.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.db.seckill.mapper.OrderMapper;
import com.db.seckill.mapper.SeckillMapper;
import com.db.seckill.mapper.entity.OrderEntity;
import com.db.seckill.mapper.entity.SeckillEntity;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

/**
 * 消费者
 */
@Component
@Slf4j
public class SeckillConsumer {

    private final SeckillMapper seckillMapper;
    private final OrderMapper orderMapper;

    public SeckillConsumer(SeckillMapper seckillMapper, OrderMapper orderMapper) {
        this.seckillMapper = seckillMapper;
        this.orderMapper = orderMapper;
    }

    /**
     * 修改库存，增加秒杀订单
     * @param message
     * @param headers
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = "modify_inventory_queue")
    @Transactional
    public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), "UTF-8");
        log.info(">>>messageId:{},msg:{}", messageId, msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);

        // 1.获取秒杀id
        Long seckillId = jsonObject.getLong("seckillId");
        SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity == null) {
            log.warn("seckillId:{},商品信息不存在!", seckillId);
            return;
        }
        Long version = seckillEntity.getVersion();
        int inventoryDeduction = seckillMapper.inventoryDeduction(seckillId, version);
        if (!toDaoResult(inventoryDeduction)) {
            log.info(">>>seckillId:{}修改库存失败>>>>inventoryDeduction返回为{} 秒杀失败！", seckillId, inventoryDeduction);
            return;
        }
        // 2.添加秒杀订单
        OrderEntity orderEntity = new OrderEntity();
        String phone = jsonObject.getString("phone");
        orderEntity.setUserPhone(phone);
        orderEntity.setSeckillId(seckillId);
        orderEntity.setState(1l);
        int insertOrder = orderMapper.insertOrder(orderEntity);
        if (!toDaoResult(insertOrder)) {
            return;
        }
        log.info(">>>修改库存成功seckillId:{}>>>>inventoryDeduction返回为{} 秒杀成功", seckillId, inventoryDeduction);
    }

    // 调用数据库层判断
    public Boolean toDaoResult(int result) {
        return result > 0 ? true : false;
    }

}
