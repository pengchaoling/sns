package com.pengchaoling.async;

import com.alibaba.fastjson.JSONObject;
import com.pengchaoling.util.JedisAdapter;
import com.pengchaoling.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Author: Lying
 * Data: 2017-02-21
 * description: 时间的入口，生产者，用于插入事件队列
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 插入队列
     */
    public boolean fireEvent(EventModel eventModel) {
        try {
            //把对象序列化之后放进队列
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
