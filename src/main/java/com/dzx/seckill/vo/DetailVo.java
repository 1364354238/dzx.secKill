package com.dzx.seckill.vo;

import com.dzx.seckill.persistence.bean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @description: 详情返回对象
 * @author: dzx
 * @date: 2022/3/11
 */

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {

    private User user;
    private GoodsVo goodsVo;
    private int seckillStatus;
    private int remainSeconds;
}
