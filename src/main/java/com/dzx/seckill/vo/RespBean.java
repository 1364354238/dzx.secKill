package com.dzx.seckill.vo;

import com.dzx.seckill.enums.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object object;

    public static RespBean success(){
        return new RespBean(RespBeanEnum.Success.getCode(), RespBeanEnum.Success.getMessage(),null);
    }

    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.Success.getCode(), RespBeanEnum.Success.getMessage(),obj);

    }

    public static RespBean error(){
        return new RespBean(RespBeanEnum.Error.getCode(), RespBeanEnum.Error.getMessage(),null);
    }

    public static RespBean error(Object obj){
        return new RespBean(RespBeanEnum.Error.getCode(), RespBeanEnum.Error.getMessage(),obj);
    }
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(),null);
    }
}
