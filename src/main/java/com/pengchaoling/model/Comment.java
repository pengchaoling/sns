package com.pengchaoling.model;

import java.util.Date;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description: 微博评论表
 */
public class Comment {
    private int id;
    private String content;     //评论内容
    private Date time;          //评论时间
    private int uid;           //评论者id
    private int wid;           //关联微博id

    public Comment(){};

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getContent(){
        return this.content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public Date getTime(){
        return this.time;
    }

    public void setTime(Date time){
        this.time = time;
    }

    public int getUid(){
        return this.uid;
    }

    public void setUid(int uid){
        this.uid = uid;
    }

    public int getWid(){
        return this.wid;
    }

    public void setWid(int wid){
        this.wid = wid;
    }


}
