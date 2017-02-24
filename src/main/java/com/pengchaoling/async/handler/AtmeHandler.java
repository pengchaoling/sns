package com.pengchaoling.async.handler;

import com.pengchaoling.async.EventHandler;
import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventType;
import com.pengchaoling.model.Atme;
import com.pengchaoling.model.UserInfo;
import com.pengchaoling.service.AtmeService;
import com.pengchaoling.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Lying
 * Data: 2017-02-25
 * description: 艾特用户异步事件处理
 */
@Component
public class AtmeHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(AtmeHandler.class);
    @Autowired
    AtmeService atmeService;

    @Autowired
    UserInfoService userInfoService;

    @Override
    public void doHandle(EventModel model) {
        try {
            String content = model.getExt("content");
            //正则表达式匹配出所有AT用户
            Pattern pt = Pattern.compile("@(([\\u4E00-\\u9FA5]|[\\uFE30-\\uFFA0]|[a-zA-Z])+(|\\s|，|。|？|；|！|‘|’|“|”)+?)");
            Matcher mt = pt.matcher(content);
            while (mt.find()) {
                UserInfo userInfo = userInfoService.getUserInfoByNickname(mt.group().replace("@",""));
                if(userInfo != null){
                    Atme atme = new Atme();
                    atme.setUid(userInfo.getUid());
                    atme.setEntityType(model.getEntityType());
                    atme.setEntityId(model.getEntityId());
                    atmeService.addAtme(atme);
                }
            }

        } catch (Exception e) {
            logger.error("增加艾特用户引失败");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.ADDCOMMENT, EventType.ADDWEIBO});
    }
}
