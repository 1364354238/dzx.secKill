# 基础秒杀项目

## 使用方式
- 1. 修改application.yml里的MySQL,redis配置
- 2. 导入resource/console.sql文件
- 3. 执行 utils/UserUtil.java文件，导入用户信息。
- 4. 启动SeckillApplication.java

# 基础秒杀项目介绍

> 秒杀主要解决两个问题，一个是并发读，一个是并发写。


> 秒杀项目要保证“稳，准，快”，也就是高可用，一致性，高性能。


## 并发读

> 核心优化理念是：尽量减少用户到服务端读数据，或者让他们读更少的数据


## 并发写

> 它要求我们在数据库层面独立出来一个库，做特殊处理。


## 秒杀流程

> 项目初始化时，将商品秒杀库存加入到Redis中


### 登录模块

> 前端传入用户名和加密后的密码，为用户生成cookie，将cookie和用户信息存入Redis中


### 商品模块

> 商品页面HTML存到Redis中，直接返回HTML


> 商品详情页面静态化，将前端需要的数据封装成一个对象DetailVo


### 秒杀模块

1. 计数器算法，接口限流
2. 秒杀前验证码校验，分流+排除部分脚本
3. 隐藏秒杀地址，为每个用户分配一个秒杀地址，只有点击秒杀按钮才会跳转到对应的秒杀地址，并且跳转前会将地址与Redis缓存校验，校验通过才会开始秒杀
4. 将订单信息存入Redis缓存中，防止超买
5. 库存量使用Redis自减，防止超卖

### 订单模块

> 使用消息队列来下订单，将订单信息存储在数据库中，没有及时处理的订单进入轮询。




[分布式session问题](学习总结/%E5%88%86%E5%B8%83%E5%BC%8Fsession%E9%97%AE%E9%A2%98/%E5%88%86%E5%B8%83%E5%BC%8Fsession%E9%97%AE%E9%A2%98.md)

[SpringMVC优化](学习总结/SpringMVC%E4%BC%98%E5%8C%96/SpringMVC%E4%BC%98%E5%8C%96.md)

[缓存](学习总结/%E7%BC%93%E5%AD%98/%E7%BC%93%E5%AD%98.md)

[并发问题](学习总结/%E5%B9%B6%E5%8F%91%E9%97%AE%E9%A2%98/%E5%B9%B6%E5%8F%91%E9%97%AE%E9%A2%98.md)

[优化](学习总结/%E4%BC%98%E5%8C%96/%E4%BC%98%E5%8C%96.md)
