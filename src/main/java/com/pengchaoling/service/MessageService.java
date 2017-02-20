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

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
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
