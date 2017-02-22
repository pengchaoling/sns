package com.pengchaoling.model;

import java.util.Date;

/**
 * Author: Lying
 * Data: 2017-02-17
 * description: 关注用户分组
 */
public class FocusGroup {
    private int id;
    private String name;
    private int uid;

    public FocusGroup(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
