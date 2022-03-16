package com.dzx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzx.seckill.persistence.bean.Goods;
import com.dzx.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */

public interface GoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
