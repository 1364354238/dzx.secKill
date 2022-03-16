package com.dzx.seckill.controller;

import com.dzx.seckill.persistence.bean.Goods;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.service.GoodsService;
import com.dzx.seckill.service.UserService;
import com.dzx.seckill.vo.DetailVo;
import com.dzx.seckill.vo.GoodsVo;
import com.dzx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @description: 商品页面
 * @author: dzx
 * @date: 2022/3/7
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

//    /**
//     * 跳转到商品列表页面
//     * @param model
////     * @param ticket 用户的UUID
//     * @return
//     */
//    @RequestMapping("/toList")
////    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue("userTicket") String ticket) {
//////        通过session获取用户信息
//////        if (StringUtils.isEmpty(ticket)||null==(User) session.getAttribute(ticket)) {
//////            return "login";
//////        }
////        if(StringUtils.isEmpty(ticket)){
////            return "login";
////        }
////        User user=userService.getUserByCookie(ticket, request, response);
////        if(null==user){
////            return "login";
////        }
////        model.addAttribute("user", user);
////        return "goodsList";
////    }
//    public String toList(Model model,User user){
//        model.addAttribute("user", user);
//        model.addAttribute("goodsList", goodsService.findGoodsVo());
//        return "goodsList";
//    }


    /**
     * 从Redis缓存中获取页面
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,User user,HttpServletResponse response,HttpServletRequest request) {
//        从Redis中获取页面，如果不为空，返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
//        Redis缓存中没有则需要手动渲染，并存入Redis中
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html=thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if (!StringUtils.isEmpty(html)) {
//            存入Redis，并设置失效时间
            valueOperations.set("goodsList", html,60, TimeUnit.SECONDS);
        }
        return html;

    }
    /**
     * 跳转商品详情
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/toDetail/{goodsId}")
//    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response) {
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
//        ValueOperations valueOperations = redisTemplate.opsForValue();
////        从Redis缓存中取
//        String html = (String) valueOperations.get("goodsDetail" + goodsId);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }
//        model.addAttribute("user", user);
//        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goods", goods);
        Date startDate = goods.getStartTime();
        Date endDate = goods.getEndTime();
        Date nowDate = startDate;
        //秒杀状态
        int secKillStatus = 0;
        //剩余开始时间
        int remainSeconds = 0;
        //秒杀还未开始
        if (nowDate.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime()-nowDate.getTime())/1000);
        // 秒杀已结束
        } else if (nowDate.after(endDate)) {
            secKillStatus = 2;
            remainSeconds = -1;
        // 秒杀中
        } else {
            secKillStatus = 1;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goods);
        detailVo.setSeckillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);

//        model.addAttribute("secKillStatus",secKillStatus);
//        model.addAttribute("remainSeconds",remainSeconds);
//        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
//        html=thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
//        if (!StringUtils.isEmpty(html)) {
//            valueOperations.set("goodsDetails",html,60,TimeUnit.SECONDS);
//        }
//        return html;
    }
}
