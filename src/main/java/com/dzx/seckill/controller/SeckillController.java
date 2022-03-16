package com.dzx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dzx.seckill.annocation.AccessLimit;
import com.dzx.seckill.enums.RespBeanEnum;
import com.dzx.seckill.exception.GlobalException;
import com.dzx.seckill.persistence.bean.Order;
import com.dzx.seckill.persistence.bean.SeckillMessage;
import com.dzx.seckill.persistence.bean.SeckillOrder;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.persistence.mapper.GoodsMapper;
import com.dzx.seckill.rabbitmq.MQSender;
import com.dzx.seckill.service.GoodsService;
import com.dzx.seckill.service.OrderService;
import com.dzx.seckill.service.SeckillOrderService;
import com.dzx.seckill.utils.JsonUtil;
import com.dzx.seckill.vo.GoodsVo;
import com.dzx.seckill.vo.RespBean;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: 秒杀控制器
 * @author: dzx
 * @date: 2022/3/9
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;
    @Autowired
    SeckillOrderService seckillOrderService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MQSender mqSender;
    @Autowired
    RedisScript<Long> redisScript;
//    记录商品库存是否为空
    private Map<Long, Boolean> emptyStock = new HashMap<>();

    /**
     * 秒杀功能
     * 优化前 QPS 352.5,库存有负数
     * 优化后 QPS 1212.4
     * @param user
     * @param goodsId
     * @return
     */


    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path, User user, Long goodsId) {
        if (user==null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
//        校验秒杀地址
        boolean check = orderService.checkPath(user, goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        if(emptyStock.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
//        预减库存,lua脚本
//        Long stock = (Long) redisTemplate.execute(redisScript,
//                Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
//        System.out.println("stock+:"+stock);
        Long stock = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
        if (stock < 0) {
            emptyStock.put(goodsId, true);
            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.send(JsonUtil.objectToString(seckillMessage));
        return RespBean.success(0);
        /*
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
//        判断是否重复抢购
//        SeckillOrder seckillOrder =
//                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",
//                        user.getId()).eq("goods_id", goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsVo.getId());
        if (seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
//        开始下订单
        Order order = orderService.secKill(user, goodsVo);
        return RespBean.success(order);

         */
    }

    /**
     * 获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(second = 5,maxCount = 5,needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(HttpServletRequest request,User user, Long goodsId, String captcha) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        /*
//        限制访问次数，5秒内访问5次
        String uri = request.getRequestURI();
        Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
        if (count == null) {
            valueOperations.set(uri + ":" + user.getId(),1,5,TimeUnit.SECONDS);
        } else if (count < 5) {
            valueOperations.increment(uri + ":" + user.getId());
        } else {
            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACHED);
        }
         */
        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user,goodsId);
        return RespBean.success(str);
    }

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId 成功，-1：秒杀失败，0：排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        System.out.println("----------------result");
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId=seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    /**
     * 系统初始化，把商品库存数量加载到Redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            emptyStock.put(goodsVo.getId(), false);
        });
    }

    /**
     * 生成验证码
     * @param user
     * @param goodsId
     * @param response
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response) {
        if (user == null || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REPEAT_ERROR);
        }
//        设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pargram", "No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);
//        生成验证码
        ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, arithmeticCaptcha.text(), 300, TimeUnit.SECONDS);
        try {
            arithmeticCaptcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败:"+e.getMessage());
        }
    }
}
