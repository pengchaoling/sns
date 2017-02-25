package com.pengchaoling.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.pengchaoling.async.EventHandler;
import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventType;
import com.pengchaoling.model.EntityType;
import com.pengchaoling.model.Feed;
import com.pengchaoling.model.UserInfo;
import com.pengchaoling.service.*;
import com.pengchaoling.util.JedisAdapter;
import com.pengchaoling.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Author: Lying
 * Data: 2017-02-23
 * description: feed异步事件处理 用与时间线
 */
@Component
public class FeedHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(AtmeHandler.class);

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    WeiboService weiboService;


    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<String ,String>();
        // 触发用户是通用的
        UserInfo actor = userInfoService.getUserInfoByUid(model.getActorId());
        if (actor == null) {
            logger.error("这里就U型了");
            return null;
        }
        map.put("uid", String.valueOf(actor.getUid()));
        map.put("nickname", actor.getNickname());
        //这是事件的类型
        if(model.getType()== EventType.FOLLOW){
            map.put("message","关注了用户");
            return JSONObject.toJSONString(map);
        }else if(model.getType()== EventType.LIKE){
            map.put("message","赞了微博");
            return JSONObject.toJSONString(map);
        }else if(model.getType()==EventType.ADDCOMMENT){
            map.put("message","评论了微博");
            return JSONObject.toJSONString(map);
        }else if(model.getType()==EventType.ADDWEIBO){
            map.put("message","发布了微博");
            return JSONObject.toJSONString(map);
        }else if(model.getType()==EventType.TURNWEIBO){
            map.put("message","转发了微博");
            return JSONObject.toJSONString(map);
        }

        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        try{

            // 构造一个新鲜事
            Feed feed = new Feed();
            feed.setCreatedDate(new Date());
            feed.setEvenType(model.getType().getValue());
            feed.setEntityType(model.getEntityType());
            feed.setEntityId(model.getEntityId());
            feed.setUid(model.getActorId());
            //厉害了
            feed.setData(buildFeedData(model));

            if (feed.getData() == null) {
                // 不支持的feed
                return;
            }
            feedService.addFeed(feed);
            // 获得所有粉丝额，其实这里还没有完善 只获取一百多个目前，后面需要完善
            List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
            //把自己也加进去，意思是自己发布东西也会插入到自己的时间线里面
            followers.add(model.getActorId());
            // 给所有粉丝推事件
            for (int follower : followers) {
                String timelineKey = RedisKeyUtil.getTimelineKey(follower);
                jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
                // 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
                long len = jedisAdapter.llen(timelineKey);
                if(len>100){
                    //删除了后面10条，这样就不需要每次超过就删一条，效率高一点
                    jedisAdapter.ltrim(timelineKey,0,10);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.LIKE, EventType.FOLLOW,EventType.ADDCOMMENT,EventType.ADDWEIBO,EventType.TURNWEIBO});

    }
}
