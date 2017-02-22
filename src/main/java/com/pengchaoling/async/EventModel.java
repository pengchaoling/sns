package com.pengchaoling.async;

import sun.java2d.pipe.hw.AccelDeviceEventNotifier;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Lying
 * Data: 2017-02-21
 * description: 时间发送的现场
 */
public class EventModel {

    private EventType type;     //事件类型       假如这是评论
    private int actorId;        //触发者         这是谁评论了
    private int entityType;     //触发的载体类型   是指评论
    private int entityId;       //触发的id       评论id
    private int entityOwnerId;  //接受者

    //扩展字段，可以用于存当时的一些状态
    private Map<String, String> exts = new HashMap<String, String>();

    public EventModel() {

    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        //用this是为了便于链式操作 直接返回的是一个对象
        return this;
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String key) {
        return exts.get(key);
    }


    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
