package com.pengchaoling.service;

import com.pengchaoling.dao.MessageDAO;
import com.pengchaoling.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-20
 * description: 站内信
 */

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message) {
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId) {
        return messageDAO.getConversationDetail(conversationId);
    }

    public List<Message> getConversationList(int userId) {
        return messageDAO.getConversationList(userId);
    }

    public int getConvesationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConvesationUnreadCount(userId, conversationId);
    }

    public int  getConvesationTotalCount(String conversationId){
        return messageDAO.getConvesationTotalCount(conversationId);
    }

    //把状态修改为已读
    public void updateHasRead(int uid,String conversationId){
        messageDAO.updateHasRead(uid,conversationId);
    }
}
