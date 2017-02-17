package com.pengchaoling.model;

import java.util.Date;

/**
 * Author: Lying
 * Data: 2017-02-15
 * description: user model
 */
public class User {
    private int id;
    private String account;         //登陆账号
    private String nickname;        //昵称
    private String password;        //密码
    private String salt;            //盐值（加盐字段）
    private Date registime;         //注册时间

    public User(){}

    public User(String account){
        this.account = account;
        this.password = "";
        this.salt = "";
    }

    public String getAccount(){
        return account;
    }

    public void setAccount(String account){
        this.account = account;
    }

    public String getNickname(){
        return this.nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getSalt(){
        return salt;
    }

    public void setSalt(String salt){
        this.salt = salt;
    }

    public Date getRegistime(){
        return registime;
    }

    public void setRegistime(Date registime){
        this.registime = registime;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }



}
