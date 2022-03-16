package com.dzx.seckill.utils;

import java.util.UUID;

/**
 * @description: 生成UUID
 * @author: dzx
 * @date: 2022/3/7
 */


public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
