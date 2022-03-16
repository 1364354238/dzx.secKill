package com.dzx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.vo.LoginVo;
import com.dzx.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dzx
 * @since 2022-03-07
 */
public interface UserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据cookie获取用户
     * @param userTicket
     * @return
     */
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    /**
     * 更新密码
     * @param userTicket
     * @param passwd
     * @param response
     * @param request
     * @return
     */
    RespBean updatePassword(String userTicket, String passwd,HttpServletResponse response,HttpServletRequest request);
}
