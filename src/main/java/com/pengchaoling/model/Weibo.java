package com.pengchaoling.model;

import java.util.Date;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description: weibo
 */
public class Weibo {
    private int id;
    private String content;         //微博内容
    private int isturn;             //是否转发（0：原创， 如果是转发的则保存该转发微博的ID）
    private Date time;              //发布时间
    private int turn;               //转发次数
    private int keep;               //收藏次数
    private int comment;            //评论次数
    private int uid;                //所属用户ID

    public Weibo(){
        this.content = "";
        this.isturn = 0;
        this.turn   = 0;
        this.keep   = 0;
        this.comment= 0;
    }

    public String getContent(){
        return this.content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public int getIsturn(){
        return this.isturn;
    }

    public void setIsturn(int isturn){
        this.isturn = isturn;
    }

    public Date getTime(){
        return this.time;
    }

    public void setTime(Date time){
        this.time = time;
    }

    public int getTurn(){
        return this.turn;
    }

    public void setTurn(int turn){
        this.turn = turn;
    }

    public int getKeep(){
        return this.keep;
    }

    public void setKeep(int keep){
        this.keep = keep;
    }

    public int getComment(){
        return this.comment;
    }

    public void setComment(int comment){
        this.comment = comment;
    }

    public int getUid(){
        return this.uid;
    }

    public void setUid(int uid){
        this.uid = uid;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }



}
