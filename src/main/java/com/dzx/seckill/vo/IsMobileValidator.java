package com.dzx.seckill.vo;

import com.dzx.seckill.annocation.IsMobile;
import com.dzx.seckill.utils.ValidatorUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @description: 手机号码校验规则
 * @author: dzx
 * @date: 2022/3/7
 */

//ConstraintValidator<注解,注释类型>
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required=false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String mobile, ConstraintValidatorContext constraintValidatorContext) {
       if(!required&&StringUtils.isEmpty(mobile)){
           return true;
       }
        return ValidatorUtil.isPhoneLegal(mobile);
    }
}
