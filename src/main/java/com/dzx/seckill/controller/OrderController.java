package com.dzx.seckill.controller;


import com.dzx.seckill.enums.RespBeanEnum;
import com.dzx.seckill.persistence.bean.Order;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.service.OrderService;
import com.dzx.seckill.vo.OrderDetailVO;
import com.dzx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVO detailVO = orderService.detail(orderId);
        return RespBean.success(detailVO);
    }
}
