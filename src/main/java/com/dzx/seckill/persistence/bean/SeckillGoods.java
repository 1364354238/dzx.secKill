package com.dzx.seckill.persistence.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_seckill_goods")
@ApiModel(value = "SeckillGoods对象", description = "")
public class SeckillGoods extends Model<SeckillGoods> {

    @ApiModelProperty("秒杀商品id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品id")
    @TableField("goods_id")
    private Long goodsId;

    @ApiModelProperty("秒杀价格")
    @TableField("seckill_price")
    private BigDecimal seckillPrice;

    @ApiModelProperty("秒杀库存")
    @TableField("stock_count")
    private Integer stockCount;

    @ApiModelProperty("秒杀开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @ApiModelProperty("秒杀结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
