package com.pengchaoling.service;

import com.pengchaoling.util.JedisAdapter;
import com.pengchaoling.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Lying
 * Data: 2017-02-21
 * description: 踩赞服务
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 返回当前like数
     */
    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }


    /**
     *  获取该用户当前微博点赞情况，如果是like则返回1，0没点赞
     */
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        return 0;
    }

    /**
     *  点赞
     */
    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        //判断是否已经点赞了，如果没赞就增加进去，赞了就删除算了
        if(getLikeStatus(userId,entityType,entityId)==0)
            jedisAdapter.sadd(likeKey, String.valueOf(userId));
        else
            jedisAdapter.srem(likeKey, String.valueOf(userId));

        //处理之后重新返回这个值，因为有可能几个人这时候都点了
        return jedisAdapter.scard(likeKey);
    }

}
