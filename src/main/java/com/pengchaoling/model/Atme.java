package com.pengchaoling.model;

/**
 * Author: Lying
 * Data: 2017-02-24
 * description: atme 艾特我
 */
public class Atme {
    private int id;
    private int uid;                        //生产者
    private int entityType;                 //触发的载体类型  评论 或 发微博
    private int entityId;                   //触发的id 评论id 或微博id


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
