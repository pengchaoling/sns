package com.pengchaoling.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Author: Lying
 * Data: 2017-02-23
 * description: timeline新鲜事 feed流
 */
public class Feed {
    private int id;
    private int uid;                        //生产者
    private Date createdDate;               //时间
    public int evenType;                    //事件类型
    private int entityType;                 //触发的载体类型
    private int entityId;                   //触发的id
    private String data;                    //数据
    private JSONObject dataJSON = null;     //json数据格式

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEvenType(){
        return evenType;
    }

    public void setEvenType(int evenType){
        this.evenType = evenType;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId(){
        return entityId;
    }

    public void setEntityId(int entityId){
        this.entityId = entityId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }
    //辅助办法，通过这个来读取那个json串里面的每个字段
    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
