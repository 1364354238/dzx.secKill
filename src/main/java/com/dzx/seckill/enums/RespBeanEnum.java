package com.dzx.seckill.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 状态码
 */
@ToString
@AllArgsConstructor
@Getter
public enum RespBeanEnum {
//    通用
    Success(200, "SUCCESS"),
    Error(500,"服务端异常"),
//  登录模块
    LOGIN_ERROR(500210,"用户名或密码不正确"),
    MOBILE_ERRO(500211, "手机号码格式不正确"),
    MOBILE_NOT_EXIST(500213,"手机号码不存在"),
    PASSWD_UPDATE_FAIL(500214, "密码更新失败"),
    SESSION_ERROR(500215,"用户不存在"),
//   参数校验
    BIND_ERROR(500212,"参数校验异常"),
    //  秒杀模块5005
    EMPTY_STOCK(500500, "空库存"),
    REPEAT_ERROR(500501,"该商品限购一件"),
    REQUEST_ILLEGAL(500502,"请求非法，请重试"),
    ERROR_CAPTCHA(500503,"验证码错误"),
    ACCESS_LIMIT_REACHED(500504,"访问过于频繁,请稍后再试"),
    //  订单模块
    ORDER_NOT_EXIST(500300, "订单信息不存在");

    private final Integer code;
    private final String message;
}
