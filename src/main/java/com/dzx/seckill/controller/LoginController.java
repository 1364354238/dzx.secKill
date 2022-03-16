package com.dzx.seckill.controller;


import com.dzx.seckill.service.UserService;
import com.dzx.seckill.vo.LoginVo;
import com.dzx.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>
 *  登录页面
 * </p>
 *
 * @author dzx
 * @since 2022-03-07
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    UserService userService;
    /**
     * 跳转登陆页面
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 登录功能
     * @param loginVo
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        return userService.doLogin(loginVo,request,response);
    }
}
