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
@TableName("t_seckill_order")
@ApiModel(value = "SeckillOrder对象", description = "")
public class SeckillOrder extends Model<SeckillOrder> {

    @ApiModelProperty("秒杀订单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品id")
    @TableField("goods_id")
    private Long goodsId;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("订单id")
    @TableField("order_id")
    private Long orderId;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
