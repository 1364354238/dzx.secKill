package com.dzx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzx.seckill.enums.RespBeanEnum;
import com.dzx.seckill.exception.GlobalException;
import com.dzx.seckill.persistence.bean.Order;
import com.dzx.seckill.persistence.bean.SeckillGoods;
import com.dzx.seckill.persistence.bean.SeckillOrder;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.persistence.mapper.OrderMapper;
import com.dzx.seckill.service.GoodsService;
import com.dzx.seckill.service.OrderService;
import com.dzx.seckill.service.SeckillGoodsService;
import com.dzx.seckill.service.SeckillOrderService;
import com.dzx.seckill.utils.MD5Util;
import com.dzx.seckill.utils.UUIDUtil;
import com.dzx.seckill.vo.GoodsVo;
import com.dzx.seckill.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    SeckillOrderService seckillOrderService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 秒杀
     * @param user
     * @param goodsVo
     * @return
     */
    @Override
    public Order secKill(User user, GoodsVo goodsVo) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
//        秒杀对象
        SeckillGoods seckillGoods=seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
                .eq("goods_id",goodsVo.getId()));
//        减库存
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//        保证库存>0才更新
        boolean seckillResult=seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count="+"stock_count-1")
                .eq("id",seckillGoods.getId())
                .gt("stock_count",0));
        if (!seckillResult) {
            valueOperations.set("isStockEmpty:"+goodsVo.getId(),"0");
            return null;
        }
//        生成订单
        Order order = new Order()
                .setUserId(user.getId())
                .setGoodsCount(1)
                .setGoodsId(seckillGoods.getGoodsId())
                .setDeliveryAddrId(0L)
                .setGoodsName(goodsVo.getGoodsName())
                .setGoodsPrice(seckillGoods.getSeckillPrice())
                .setOrderChannel(1)
                .setStatus(0)
                .setCreateDate(new Date());
        orderMapper.insert(order);
//        生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder()
                .setOrderId(order.getId())
                .setGoodsId(goodsVo.getId())
                .setUserId(user.getId());
        seckillOrderService.save(seckillOrder);
        valueOperations.set("order:" + user.getId() + ":" + goodsVo.getId(), seckillOrder,60, TimeUnit.SECONDS);
        return order;
    }

    @Override
    public OrderDetailVO detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        return new OrderDetailVO(order,goodsVo);
    }

    /**
     * 获取秒杀地址
     * @return
     */
    @Override
    public String createPath(User user,Long goodsId) {
        String path = MD5Util.encryptForm(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, path, 60, TimeUnit.SECONDS);
        return path;
    }

    /**
     * 校验秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public boolean checkPath(User user, Long goodsId,String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath= (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    /**
     * 校验验证码
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(captcha)) {
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}
