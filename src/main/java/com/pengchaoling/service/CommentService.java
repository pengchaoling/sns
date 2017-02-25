package com.pengchaoling.service;

import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventProducer;
import com.pengchaoling.async.EventType;
import com.pengchaoling.dao.CommentDAO;
import com.pengchaoling.model.Comment;
import com.pengchaoling.model.EntityType;
import com.pengchaoling.model.HostHolder;
import com.pengchaoling.model.UserInfo;
import com.pengchaoling.util.SnsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Autowired
    UserInfoService userInfoService;

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
        //替换艾特
        Comment comment = commentDAO.getCommentById(id);
        comment.setContent(replaceAtme(comment.getContent()));
        return comment;
    }


    public List<Comment>  selectCommentsByWid(int wid){
        List<Comment>  comments= commentDAO.selectCommentsByWid(wid);
        List<Comment> news = new ArrayList<Comment>();
        if(!comments.isEmpty()) {
            for (Comment comment : comments) {
                comment.setContent(replaceAtme(comment.getContent()));
                news.add(comment);
            }
        }
        return news;
    }

    //辅助方法 提供给本类使用，替换@用户名 以及替换表情包
    private String replaceAtme(String content){
        //正则表达式匹配出所有AT用户
        Pattern pt = Pattern.compile("@(([\\u4E00-\\u9FA5]|[\\uFE30-\\uFFA0]|[a-zA-Z])+(|\\s|，|。|？|；|！|‘|’|“|”)+?)");
        Matcher mt = pt.matcher(content);
        while (mt.find()) {
            UserInfo userInfo = userInfoService.getUserInfoByNickname(mt.group().replace("@",""));
            if(userInfo != null){
                String str = "<a href='/profile/"+userInfo.getUid()+"'>"+mt.group()+"</a>";
                content = content.replace(mt.group(),str);
            }
        }
        content = SnsUtil.emojiReplace(content);
        return content;
    }
}
