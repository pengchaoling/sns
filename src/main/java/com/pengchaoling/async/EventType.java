package com.pengchaoling.async;

/**
 * Author: Lying
 * Data: 2017-02-21
 * description: 事件类型
 */
public enum EventType {
    LIKE(0),        //点赞
    COMMENT(1),     //评论
    WEIBO(2),       //微博
    MAIL(3),        //邮件
    FOLLOW(4),      //关注
    UNFOLLOW(5),    //取消关注
    ADDWEIBO(6),    //新增微博
    ADDUSER(7),     //增加用户
    ADDCOMMENT(8),  //增加评论
    TURNWEIBO(9);      //转发微博

    private int value;
    EventType(int value) { this.value = value; }
    public int getValue() { return value; }
}
