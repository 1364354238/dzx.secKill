package com.dzx.seckill.config;

import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.service.UserService;
import com.dzx.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 自定义用户参数
 * @author: dzx
 * @date: 2022/3/8
 */

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

//    @Autowired
//    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?>clazz=methodParameter.getParameterType();
        return clazz == User.class; //方法的参数为User，会执行resolveArgument方法
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
//        HttpServletRequest request=nativeWebRequest.getNativeRequest(HttpServletRequest.class);
//        String ticket = CookieUtil.getCookieValue(request, "userTicket");
//        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
//        if(StringUtils.isEmpty(ticket)){
//            return null;
//        }
//        return userService.getUserByCookie(ticket,request, response );
        return UserContext.getUser();
    }
}
