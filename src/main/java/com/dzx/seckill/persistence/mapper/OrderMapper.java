package com.dzx.seckill.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzx.seckill.persistence.bean.Order;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
