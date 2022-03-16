package com.dzx.seckill.config;

import com.dzx.seckill.persistence.bean.User;

/**
 * @description: 当前线程存当前用户
 * @author: dzx
 * @date: 2022/3/16
 */


public class UserContext {
//    当前线程
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUserThreadLocal(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }
}
