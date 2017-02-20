package com.pengchaoling.service;

import com.pengchaoling.dao.CommentDAO;
import com.pengchaoling.model.Comment;
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

    public int addComment(Comment comment){
        //html过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //敏感词 过滤掉
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment);
    }



    public List<Comment>  selectCommentsByWid(int wid){
        return commentDAO.selectCommentsByWid(wid);
    }


}
