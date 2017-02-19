package com.pengchaoling.service;

import com.pengchaoling.dao.PictureDAO;
import com.pengchaoling.dao.WeiboDAO;
import com.pengchaoling.model.Picture;
import com.pengchaoling.model.Weibo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    SensitiveService sensitiveService;

    public int addWeibo(Weibo weibo){
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

    public List<Weibo>  selectWeibos(int offset,int limit){
        return weiboDAO.selectWeibos(offset,limit);
    }


}
