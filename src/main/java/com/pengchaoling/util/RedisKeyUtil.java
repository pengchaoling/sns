package com.pengchaoling.util;

/**
 * Author: Lying
 * Data: 2017-02-21
 * description: 专门用于管理redis的key  所有的key都是从这里去的
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";                //喜欢
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";   //事件队列

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }


    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

}
