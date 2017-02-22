package com.pengchaoling.controller;

import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventProducer;
import com.pengchaoling.async.EventType;
import com.pengchaoling.model.*;
import com.pengchaoling.service.CommentService;
import com.pengchaoling.service.FollowService;
import com.pengchaoling.service.UserInfoService;
import com.pengchaoling.service.UserService;
import com.pengchaoling.util.SnsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Lying
 * Data: 2017-02-22
 * description:关注操作控制器 两个方法都是提供前台ajax操作的
 */
@Controller
public class FollowController {
    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    /**
     *  关注用户
     */
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String followUser(@RequestParam("uid") int uid) {

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, uid);
        //关注事件，写入消息队列
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(uid)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(uid));

        // 返回关注的人数
        return SnsUtil.getJSONString(ret ? 1: 0, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }
    /**
     *  取消关注
     */
    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("uid") int uid) {

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, uid);
        //关注事件写入事件队列
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(uid)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(uid));

        // 返回关注的人数
        return SnsUtil.getJSONString(ret ? 1 : 0, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

}
