package com.pengchaoling.async.handler;

import com.pengchaoling.async.EventHandler;
import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventType;
import com.pengchaoling.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-24
 * description: 新增用户 新增索引
 */
@Component
public class AddUserHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddUserHandler.class);
    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        try {
            searchService.indexUserInfo(model.getActorId(),model.getEntityId(),
                    model.getExt("nickname"));
        } catch (Exception e) {
            logger.error("增加微博索引失败");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADDUSER);
    }
}
