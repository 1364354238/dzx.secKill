package com.dzx.seckill.rabbitmq;

import com.dzx.seckill.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @description: 消息发送者
 * @author: dzx
 * @date: 2022/3/11
 */


@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Object msg) {
        log.info("发送消息:"+msg);
        rabbitTemplate.convertAndSend("seckillExchange","seckill.message", msg);
    }
}
