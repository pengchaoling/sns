package com.pengchaoling.model;

import java.security.PublicKey;
import java.util.Date;

/**
 * Author: Lying
 * Data: 2017-02-15
 * description: 用户基本信息
 */
public class UserInfo {
    private int id;
    private String nickname;        //用户昵称
    private String truename;        //真实姓名
    private int sex;                //性别 0保密，1男，2女
    private String location;        //所在地
    private String constellation;   //星座
    private String intro;           //一句话介绍自己
    private String face50;          //50*50头像
    private String face80;          //80*80头像
    private String face180;         //180*180头像
    private String style;           //个性模板
    private int follow;             //关注数
    private int fans;               //粉丝数
    private int weibo;              //微博数
    private int uid;                //关联用户ID


    public UserInfo(){
        this.nickname = "";
        this.truename = "";
        this.sex = 0;
        this.location = "";
        this.constellation = "";
        this.intro = "";
        this.face50 = "";
        this.face80 = "";
        this.face180 = "";
        this.style = "default";
        this.follow = 0;
        this.fans = 0;
        this.weibo = 0;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }


    public String getNickname(){
        return this.nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getTruename(){
        return this.truename;
    }

    public void setTruename(String truename){
        this.truename = truename;
    }

    public int getSex(){
        return this.sex;
    }

    public void setSex(int sex){
        this.sex = sex;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getConstellation(){
        return this.constellation;
    }

    public void setConstellation(String constellation){
        this.constellation = constellation;
    }

    public String getIntro(){
        return this.intro;
    }

    public void setIntro(String intro){
        this.intro = intro;
    }

    public String getFace50(){
        return this.face50;
    }

    public void setFace50(String face50){
        this.face50 = face50;
    }

    public String getFace80(){
        return this.face80;
    }

    public void setFace80(String face80){
        this.face80 = face80;
    }

    public String getFace180(){
        return this.face180;
    }

    public void setFace180(String face180){
        this.face180 = face180;
    }

    public String getStyle(){
        return this.style;
    }

    public void setStyle(String style){
        this.style = style;
    }

    public int getFollow(){
        return this.follow;
    }

    public void setFollow(int follow){
        this.follow = follow;
    }

    public int getFans(){
        return this.fans;
    }

    public void setFans(int fans){
        this.fans = fans;
    }

    public int getWeibo(){
        return this.weibo;
    }

    public void setWeibo(int weibo){
        this.weibo=weibo;
    }

    public int getUid(){
        return this.uid;
    }

    public void setUid(int uid){
        this.uid = uid;
    }


}
