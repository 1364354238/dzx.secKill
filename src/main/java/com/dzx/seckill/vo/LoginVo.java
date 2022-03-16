package com.dzx.seckill.vo;

import com.dzx.seckill.annocation.IsMobile;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class LoginVo {
    @NotNull
    @IsMobile(required = true)
    private String mobile;
    @NotNull
    @Length(min=32)
    private String password;
}
