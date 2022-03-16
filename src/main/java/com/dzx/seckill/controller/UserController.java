package com.dzx.seckill.controller;

import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.rabbitmq.MQSender;
import com.dzx.seckill.vo.RespBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 前端控制器
 * @author: dzx
 * @date: 2022/3/10
 */

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    MQSender mqSender;
    /**
     * 用户信息（测试）
     * @param user
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user) {
        return RespBean.success(user);
    }

    /**
     * 测试rabbitmq
     */
    @RequestMapping("/mq")
    @ResponseBody
    public void mq() {
        mqSender.send("hello");
    }
}
