package com.dzx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzx.seckill.persistence.bean.Order;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.vo.GoodsVo;
import com.dzx.seckill.vo.OrderDetailVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */
public interface OrderService extends IService<Order> {

    Order secKill(User user, GoodsVo goodsVo);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderDetailVO detail(Long orderId);

    String createPath(User user,Long goodsId);

    boolean checkPath(User user, Long goodsId,String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
