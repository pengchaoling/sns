package com.pengchaoling.model;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description: 微博配图
 */
public class Picture {
    private int id;
    private String mini;    //小图
    private String medium;  //中图
    private String max;     //大图
    private int wid;        //所属微博id

    public Picture(){
        this.mini = "";
        this.medium = "";
        this.max = "";
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getMini(){
        return this.mini;
    }

    public void setMini(String mini){
        this.mini = mini;
    }

    public String getMedium(){
        return this.medium;
    }

    public void setMedium(String medium){
        this.medium = medium;
    }

    public String getMax(){
        return max;
    }

    public void setMax(String max){
        this.max = max;
    }

    public int getWid(){
        return this.wid;
    }

    public void setWid(int wid){
        this.wid = wid;
    }



}
