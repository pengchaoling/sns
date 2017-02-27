package com.pengchaoling.service;

import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventProducer;
import com.pengchaoling.async.EventType;
import com.pengchaoling.dao.KeepDAO;
import com.pengchaoling.dao.PictureDAO;
import com.pengchaoling.dao.WeiboDAO;
import com.pengchaoling.model.*;
import com.pengchaoling.util.SnsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description:
 */
@Service
public class WeiboService {
    private static final Logger logger = LoggerFactory.getLogger(WeiboService.class);

    @Autowired
    WeiboDAO weiboDAO;
    @Autowired
    PictureDAO pictureDAO;
    @Autowired
    KeepDAO keepDAO;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserInfoService userInfoService;


    public int addWeibo(Weibo weibo){
        //html过滤
        weibo.setContent(HtmlUtils.htmlEscape(weibo.getContent()));
        //敏感词 过滤掉
        weibo.setContent(sensitiveService.filter(weibo.getContent()));
        int res = weiboDAO.addWeibo(weibo);
        if(res>0) {
            EventType eventType = weibo.getIsturn() > 0 ? EventType.TURNWEIBO : EventType.ADDWEIBO;
            //提交到异步事件队列去处理
            eventProducer.fireEvent(new EventModel(eventType)
                    .setActorId(hostHolder.getUser().getId()).setEntityId(weibo.getId())
                    .setEntityType(EntityType.ENTITY_WEIBO).setEntityOwnerId(weibo.getUid())
                    .setExt("content", String.valueOf(weibo.getContent())));
        }
        return res;
    }

    public int addPicture(Picture picture){
        return pictureDAO.addWPicture(picture);
    }

    public Picture selectPictureByWid(int wid){
        return pictureDAO.selectPictureByWid(wid);
    }

    public Weibo selectWeiboById(int id){
        Weibo weibo = weiboDAO.selectWeiBoById(id);
        if(weibo!=null)
            weibo.setContent(replaceAtme(weibo.getContent()));
        return weibo;
    }

    public List<Weibo>  selectWeibos(){
        List<Weibo> weibos = weiboDAO.selectWeibos();
        if(!weibos.isEmpty()){
            for(int i=0;i<weibos.size();i++){
                String content ="";
                if(weibos.get(i)!=null){
                    content = replaceAtme(weibos.get(i).getContent());
                }
                weibos.get(i).setContent(content);
            }

        }
        return weibos;
    }

    public List<Weibo> selectWeibosByUid(int uid){

        List<Weibo> weibos = weiboDAO.selectWeibosByUid(uid);
        if(!weibos.isEmpty()){
            for(int i=0;i<weibos.size();i++){
                String content ="";
                if(weibos.get(i)!=null){
                     content = replaceAtme(weibos.get(i).getContent());
                }
                weibos.get(i).setContent(content);
            }

        }

        return weibos;
    }

    public void IncTurn(int wid){
        weiboDAO.IncTurn(wid);
    }

    public void IncKeep(int wid){
        weiboDAO.IncKeep(wid);
    }

    public void IncComment(int wid){
        weiboDAO.IncComment(wid);
    }

    //微博收藏相关操作
    public int addKeep(Keep keep){
        return keepDAO.addKeep(keep);
    }

    public List<Keep> selectKeepsByuid(int uid){
        return keepDAO.selectKeepsByuid(uid);
    }

    public Keep selectKeepByUidWid(Keep keep){
        return keepDAO.selectKeepByUidWid(keep);
    }

    public void delectWeiboByWid(int wid){
        weiboDAO.deleteWeiboById(wid);
    }

    //辅助方法 提供给本类使用，替换@用户名 以及替换 表情包
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
