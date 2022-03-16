package com.dzx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzx.seckill.persistence.bean.SeckillOrder;
import com.dzx.seckill.persistence.bean.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */
public interface SeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
