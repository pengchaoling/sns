package com.pengchaoling.model;

import org.springframework.stereotype.Component;

/**
 * Author: Lying
 * Data: 2017-02-16
 * description: 用户连接池，负责管理用户连接信息，提供给拦截器使用
 */
@Component  //普通实例的依赖注入
public class HostHolder {
    //为每个线程提供独立的变量副本
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
