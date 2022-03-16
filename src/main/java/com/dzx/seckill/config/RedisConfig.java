package com.dzx.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.UnknownHostException;

/**
 * @description: redis配置
 * @author: dzx
 * @date: 2022/3/7
 */


@Configuration
public class RedisConfig {

    @Resource
    RedisConnectionFactory connectionFactory;
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//key序列器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//value序列器
        redisTemplate.setValueSerializer(new
                GenericJackson2JsonRedisSerializer());
//Hash类型 key序列器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//Hash类型 value序列器
        redisTemplate.setHashValueSerializer(new
                GenericJackson2JsonRedisSerializer());
        System.out.println("connection"+connectionFactory);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public DefaultRedisScript<Long> script() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//        lock.lua脚本位置
        redisScript.setLocation(new ClassPathResource("stock.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}