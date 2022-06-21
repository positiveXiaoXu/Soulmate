package com.xugm.server.interceptor;

import com.xugm.model.domain.User;

/**
 * 工具类：实现向threadlocal存储数据的方法
 */
public class UserHolder {

    private static ThreadLocal<User> tl = new ThreadLocal<>();


    //将用户对象，存入Threadlocal
    public static void set(User user) {
        tl.set(user);
    }

    //从当前线程，获取用户对象
    public static User get() {
        return tl.get();
    }

    //从当前线程，获取用户对象的id
    public static Long getUserId() {
        return tl.get().getId();
    }

    //从当前线程，获取用户对象的手机号码
    public static String getMobile() {
        return tl.get().getMobile();
    }
}
