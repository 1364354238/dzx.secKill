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
@TableName("t_goods")
@ApiModel(value = "Goods对象", description = "")
public class Goods extends Model<Goods> {

    @ApiModelProperty("商品id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品名称")
    @TableField("goods_name")
    private String goodsName;

    @ApiModelProperty("商品标题")
    @TableField("goods_title")
    private String goodsTitle;

    @ApiModelProperty("商品图片")
    @TableField("goods_img")
    private String goodsImg;

    @ApiModelProperty("商品详情")
    @TableField("goods_detail")
    private String goodsDetail;

    @ApiModelProperty("商品价格")
    @TableField("goods_price")
    private BigDecimal goodsPrice;

    @ApiModelProperty("商品库存,-1表示无限制")
    @TableField("goods_stock")
    private Integer goodsStock;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
