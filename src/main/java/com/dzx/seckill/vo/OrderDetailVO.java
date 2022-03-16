package com.dzx.seckill.vo;

import com.dzx.seckill.persistence.bean.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 订单详情返回对象
 * @author: dzx
 * @date: 2022/3/11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVO {
    private Order order;
    private GoodsVo goodsVo;
}
