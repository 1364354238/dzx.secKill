package com.dzx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzx.seckill.enums.RespBeanEnum;
import com.dzx.seckill.exception.GlobalException;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.persistence.mapper.UserMapper;
import com.dzx.seckill.service.UserService;
import com.dzx.seckill.utils.CookieUtil;
import com.dzx.seckill.utils.JsonUtil;
import com.dzx.seckill.utils.MD5Util;
import com.dzx.seckill.utils.UUIDUtil;
import com.dzx.seckill.vo.LoginVo;
import com.dzx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;
    /**
     * 登录功能
     * @return
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String passwd = loginVo.getPassword();
        String mobile = loginVo.getMobile();
//        //参数校验，修改后采用注解的形式
//        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(passwd)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isPhoneLegal(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERRO);
//        }
        User user = userMapper.selectById(mobile);
        if(null==user){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if(!MD5Util.encryptDB(passwd,mobile).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
//        生成cookie
        String ticket = UUIDUtil.uuid();
//      将用户信息存入到Redis中
        redisTemplate.opsForValue().set("user:" + ticket, JsonUtil.objectToString(user));
//        request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    /**
     * 根据cookie获取用户
     * @param userTicket
     * @return
     */
    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(null==userTicket){
            return null;
        }
        String userJson= (String) redisTemplate.opsForValue().get("user:" + userTicket);
        if(null==userJson){
            return null;
        }
        User user = JsonUtil.jsonToObject(userJson, User.class);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    @Override
    public RespBean updatePassword(String userTicket, String passwd, HttpServletResponse response, HttpServletRequest request) {
        User user = getUserByCookie(userTicket, request, response);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.encryptDB(passwd, String.valueOf(user.getId())));
        int result = userMapper.updateById(user);
        if (1 == result) {
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWD_UPDATE_FAIL);
    }
}
