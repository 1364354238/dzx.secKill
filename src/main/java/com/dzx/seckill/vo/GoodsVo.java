package com.dzx.seckill.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dzx.seckill.persistence.bean.Goods;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description: 商品返回对象
 * @author: dzx
 * @date: 2022/3/8
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {
    @ApiModelProperty("秒杀价格")
    @TableField("seckill_price")
    private BigDecimal seckillPrice;

    @ApiModelProperty("秒杀库存")
    @TableField("stock_count")
    private Integer stockCount;

    @ApiModelProperty("秒杀开始时间")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty("秒杀结束时间")
    @TableField("end_time")
    private Date endTime;
}
