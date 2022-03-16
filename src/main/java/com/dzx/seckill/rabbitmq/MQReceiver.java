package com.dzx.seckill.rabbitmq;

import com.dzx.seckill.enums.RespBeanEnum;
import com.dzx.seckill.persistence.bean.SeckillMessage;
import com.dzx.seckill.persistence.bean.SeckillOrder;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.service.GoodsService;
import com.dzx.seckill.service.OrderService;
import com.dzx.seckill.utils.JsonUtil;
import com.dzx.seckill.vo.GoodsVo;
import com.dzx.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @description: 消息消费者
 * @author: dzx
 * @date: 2022/3/11
 */

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg) {
        log.info("seckillQueue接收消息"+msg);
        SeckillMessage seckillMessage = JsonUtil.jsonToObject(msg, SeckillMessage.class);
        if (seckillMessage == null) {
            return;
        }
        Long goodsId = seckillMessage.getGoodId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo=goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }
//        判断是否重复抢购
        String seckillOrderJson = (String)
                redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (!StringUtils.isEmpty(seckillOrderJson)) {
            return;
        }
        System.out.println("----------------------抢购");
        orderService.secKill(user, goodsVo);
    }

}
