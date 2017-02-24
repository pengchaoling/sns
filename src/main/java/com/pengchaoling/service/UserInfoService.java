package com.pengchaoling.service;

import com.pengchaoling.dao.LoginTicketDAO;
import com.pengchaoling.dao.UserInfoDAO;
import com.pengchaoling.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * Author: Lying
 * Data: 2017-02-17
 * description: UserInfoService
 */
@Service
public class UserInfoService {
    //通过以来注入方式来生成对象,不需要每次使用都new
    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Autowired
    SensitiveService sensitiveService;

    public UserInfo getUserInfoByUid(int uid) {
        return userInfoDAO.selectUserInfoByUid(uid);
    }
    //这是根据userinfo表的主键id来的
    public UserInfo getUserInfoById(int id) {
        return userInfoDAO.selectUserInfoById(id);
    }


    public UserInfo getUserInfoByNickname(String nickname){
        return userInfoDAO.selectUserInfoByNickname(nickname);
    }

    public void IncWeibo(int uid){
        userInfoDAO.IncWeibo(uid);
    }

    public void DecWeibo(int uid){
        userInfoDAO.DecWeibo(uid);
    }

    /**
     *  默认用户信息插入，在注册的时候会调用这个函数去生成默认的函数
     * @param nickname
     * @param uid
     */
    public void addDefaultUserInfo(String nickname,int uid){
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(nickname);
        userInfo.setUid(uid);
        userInfoDAO.addUserInfo(userInfo);

    }
    /**
     * 修改用户头像
     */
    public void updateFace(String face50,String face80,String face180,int uid){
        UserInfo userInfo = new UserInfo();
        userInfo.setFace50(face50);
        userInfo.setFace80(face80);
        userInfo.setFace180(face180);
        userInfo.setUid(uid);
        userInfoDAO.updateFace(userInfo);
    }
    /**
     * 修改用户基本信息
     */
    public void updateBasic(UserInfo userInfo){
        //敏感词以及html过滤
        userInfo.setNickname(HtmlUtils.htmlEscape(sensitiveService.filter(userInfo.getNickname())));
        userInfo.setTruename(HtmlUtils.htmlEscape(sensitiveService.filter(userInfo.getTruename())));
        userInfo.setIntro(HtmlUtils.htmlEscape(sensitiveService.filter(userInfo.getIntro())));

        userInfoDAO.updateBasic(userInfo);
    }
}
