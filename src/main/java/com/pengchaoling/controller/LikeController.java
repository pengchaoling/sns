package com.pengchaoling.controller;

import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventProducer;
import com.pengchaoling.async.EventType;
import com.pengchaoling.model.EntityType;
import com.pengchaoling.model.HostHolder;
import com.pengchaoling.model.Weibo;
import com.pengchaoling.service.CommentService;
import com.pengchaoling.service.LikeService;
import com.pengchaoling.service.WeiboService;
import com.pengchaoling.util.SnsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author: Lying
 * Data: 2017-02-21
 * description: 微博踩和赞
 */
@Controller
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    WeiboService weiboService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("wid") int wid) {

        try{
            Weibo weibo = weiboService.selectWeiboById(wid);


            long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_WEIBO, wid);
            int  status = likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_WEIBO, wid);
            if(status==1){
                //提交到异步事件队列去处理 通知博主被点赞了
                eventProducer.fireEvent(new EventModel(EventType.LIKE)
                        .setActorId(hostHolder.getUser().getId()).setEntityId(wid)
                        .setEntityType(EntityType.ENTITY_WEIBO).setEntityOwnerId(weibo.getUid())
                        .setExt("weibo", String.valueOf(weibo.getContent())));
                //点赞
                return SnsUtil.getJSONString(1, String.valueOf(likeCount));
            }else{
                //取消赞
                return SnsUtil.getJSONString(-1, String.valueOf(likeCount));
            }
        }catch (Exception e){
            return SnsUtil.getJSONString(0);
        }

    }

}
