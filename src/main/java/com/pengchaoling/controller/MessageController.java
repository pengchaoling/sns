package com.pengchaoling.controller;

import com.pengchaoling.model.*;
import com.pengchaoling.service.MessageService;
import com.pengchaoling.service.UserInfoService;
import com.pengchaoling.service.UserService;
import com.pengchaoling.util.SnsUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-20
 * description: 站内信
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    HostHolder hostHolder;

    /**
     * 私信对话列表
     */
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<ViewObject>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                UserInfo userinfo = userInfoService.getUserInfoByUid(targetId);
                vo.set("userinfo", userinfo);
                vo.set("unread", messageService.getConvesationUnreadCount(localUserId, msg.getConversationId()));
                vo.set("total", messageService.getConvesationTotalCount(msg.getConversationId()));
                conversations.add(vo);
            }
                model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    /**
     * 和某个人的私信详情 一条一条的
     */
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId,@Param("uid") int uid) {
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 100);
            List<ViewObject> messages = new ArrayList<>();
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                UserInfo userinfo = userInfoService.getUserInfoByUid(msg.getFromId());
                if (userinfo == null) {
                    continue;
                }
                vo.set("userinfo", userinfo);
                messages.add(vo);
            }
            //对话的用户信息
            UserInfo fromUser = userInfoService.getUserInfoByUid(uid);
            model.addAttribute("fromUser",fromUser);
            model.addAttribute("messages", messages);

            //把信息修改为已读状态
            messageService.updateHasRead(hostHolder.getUser().getId(),conversationId);
        } catch (Exception e) {
            logger.error("获取详情消息失败" + e.getMessage());
        }
        return "letterDetail";
    }


    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    public String addMessage(Model model,@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {

        try{
            UserInfo userInfo = userInfoService.getUserInfoByNickname(toName);
            if (userInfo == null) {
                model.addAttribute("error_msg", "该用户不存在");
                model.addAttribute("jump_url", "/msg/list");
                return "dispatch_jump";
            }

            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(hostHolder.getUser().getId());
            msg.setToId(userInfo.getId());
            msg.setCreatedDate(new Date());
            msg.setHasRead(0);
            //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(msg);
            model.addAttribute("success_msg", "私信发送成功");
            model.addAttribute("jump_url", "/msg/list");
            return "dispatch_jump";
        }catch (Exception e){
            logger.error("私信发送异常" + e.getMessage());
            model.addAttribute("error_msg", "私信发送失败，请重试");
            model.addAttribute("jump_url", "/msg/list");
            return "dispatch_jump";
        }
    }



}
