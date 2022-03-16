package com.dzx.seckill.persistence.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 秒杀信息
 * @author: dzx
 * @date: 2022/3/14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {
    private User user;
    private Long goodId;
}
