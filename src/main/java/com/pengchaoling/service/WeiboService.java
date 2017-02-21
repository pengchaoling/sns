package com.pengchaoling.service;

import com.pengchaoling.dao.KeepDAO;
import com.pengchaoling.dao.PictureDAO;
import com.pengchaoling.dao.WeiboDAO;
import com.pengchaoling.model.Keep;
import com.pengchaoling.model.Picture;
import com.pengchaoling.model.Weibo;
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
public class WeiboService {
    @Autowired
    WeiboDAO weiboDAO;
    @Autowired
    PictureDAO pictureDAO;
    @Autowired
    KeepDAO keepDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addWeibo(Weibo weibo){
        //html过滤
        weibo.setContent(HtmlUtils.htmlEscape(weibo.getContent()));
        //敏感词 过滤掉
        weibo.setContent(sensitiveService.filter(weibo.getContent()));
        return weiboDAO.addWeibo(weibo);
    }

    public int addPicture(Picture picture){
        return pictureDAO.addWPicture(picture);
    }

    public Picture selectPictureByWid(int wid){
        return pictureDAO.selectPictureByWid(wid);
    }

    public Weibo selectWeiboById(int id){
        return weiboDAO.selectWeiBoById(id);
    }

    public List<Weibo>  selectWeibos(){
        return weiboDAO.selectWeibos();
    }

    public List<Weibo> selectWeibosByUid(int uid){
        return weiboDAO.selectWeibosByUid(uid);
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

}
