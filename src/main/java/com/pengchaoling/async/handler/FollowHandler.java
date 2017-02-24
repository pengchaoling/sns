package com.pengchaoling.async.handler;

import com.pengchaoling.async.EventHandler;
import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventType;
import com.pengchaoling.model.EntityType;
import com.pengchaoling.model.Message;
import com.pengchaoling.model.UserInfo;
import com.pengchaoling.service.MessageService;
import com.pengchaoling.service.UserInfoService;
import com.pengchaoling.service.UserService;
import com.pengchaoling.util.SnsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-22
 * description:关注事件处理 发站内信
 */
@Component
public class FollowHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    UserInfoService userInfoService;

    @Override
    public void doHandle(EventModel model) {
        try{
            Message message = new Message();
            message.setFromId(SnsUtil.SYSTEM_USERID);
            message.setToId(model.getEntityOwnerId());
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            UserInfo userInfo = userInfoService.getUserInfoByUid(model.getActorId());
            message.setContent(userInfo.getNickname() + "关注了你");
            messageService.addMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
