package com.pengchaoling.service;

import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventProducer;
import com.pengchaoling.async.EventType;
import com.pengchaoling.dao.CommentDAO;
import com.pengchaoling.model.Comment;
import com.pengchaoling.model.EntityType;
import com.pengchaoling.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description:
 */
@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    HostHolder hostHolder;

    public int addComment(Comment comment){
        //html过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //敏感词 过滤掉
        comment.setContent(sensitiveService.filter(comment.getContent()));
        int res = commentDAO.addComment(comment);
        //提交到异步事件队列去处理
        if(res>0){
            eventProducer.fireEvent(new EventModel(EventType.ADDCOMMENT)
                    .setActorId(hostHolder.getUser().getId()).setEntityId(comment.getId())
                    .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUid())
                    .setExt("content", String.valueOf(comment.getContent())));
        }

        return commentDAO.addComment(comment);
    }

    public Comment getCommentById(int id){
        return commentDAO.getCommentById(id);
    }


    public List<Comment>  selectCommentsByWid(int wid){
        return commentDAO.selectCommentsByWid(wid);
    }


}
