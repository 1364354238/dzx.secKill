package com.dzx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzx.seckill.persistence.bean.SeckillOrder;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.persistence.mapper.SeckillOrderMapper;
import com.dzx.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements SeckillOrderService {
    @Autowired
    SeckillOrderMapper seckillOrderMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Long getResult(User user, Long goodsId) {
         SeckillOrder seckillOrder=seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id",user.getId())
                .eq("goods_id",goodsId));
        if (seckillOrder == null) {
            if(redisTemplate.hasKey("isStockEmpty:" + goodsId)){
                return -1L;
            }else {
                return 0L;
            }
        }
        return seckillOrder.getOrderId();
    }
}
