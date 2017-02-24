package com.pengchaoling.controller;

import com.pengchaoling.model.*;
import com.pengchaoling.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-24
 * description: 搜索
 */
@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;

    @Autowired
    FollowService followService;

    @Autowired
    WeiboService weiboService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;



    @RequestMapping(path = {"/Search/searchWeibo"}, method = {RequestMethod.GET})
    public String searchWeibo(Model model, @RequestParam("keyword") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "20") int count) {
        try {
            List<Weibo> weibosList = searchService.searchWeibo(keyword, offset, count,
                    "<em>", "</em>");
            List<ViewObject> vos = new ArrayList<>();
            for (Weibo wb : weibosList) {
                Weibo weibo = weiboService.selectWeiboById(wb.getId());
                ViewObject vo = new ViewObject();
                if (wb.getContent() != null) {
                    weibo.setContent(wb.getContent());
                }

                vo.set("weibo", weibo);
                vo.set("userinfo", userInfoService.getUserInfoByUid(weibo.getUid()));

                vo.set("picture",weiboService.selectPictureByWid(weibo.getId()));
                //获取是否已点赞
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_WEIBO, weibo.getId()));
                vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_WEIBO, weibo.getId()));

                //如果是转发的，则获取原微博
                if(weibo.getIsturn()>0){
                    vo.set("weiboTurn",weiboService.selectWeiboById(weibo.getIsturn()));
                    if(weiboService.selectWeiboById(weibo.getIsturn())!=null){
                        vo.set("userTurn",userInfoService.getUserInfoByUid(weiboService.selectWeiboById(weibo.getIsturn()).getUid()));
                        vo.set("turnLikeCount", likeService.getLikeCount(EntityType.ENTITY_WEIBO, weibo.getIsturn()));

                        vo.set("pictureTurn",weiboService.selectPictureByWid(weibo.getIsturn()));
                    }
                }

                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            logger.error("微博搜索" + e.getMessage());
        }
        return "searchWeibo";
    }

    @RequestMapping(path = {"/Search/searchUser"}, method = {RequestMethod.GET})
    public String searchUser(Model model, @RequestParam("keyword") String keyword,
                              @RequestParam(value = "offset", defaultValue = "0") int offset,
                              @RequestParam(value = "count", defaultValue = "20") int count) {
        try {
            List<UserInfo> userInfosList = searchService.searchUser(keyword, offset, count,
                    "<em>", "</em>");
            List<ViewObject> vos = new ArrayList<>();
            for (UserInfo userInfo : userInfosList) {
                UserInfo u = userInfoService.getUserInfoById(userInfo.getId());
                ViewObject vo = new ViewObject();
                vo.set("nickname",userInfo.getNickname());
                vo.set("userinfo",u);
                //是否已关注该用户
                boolean followed = followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER,u.getUid());
                vo.set("followed",followed);
                vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, u.getUid()));
                vo.set("followeeCount", followService.getFolloweeCount(u.getUid(), EntityType.ENTITY_USER));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            logger.error("用户搜索" + e.getMessage());
        }
        return "searchUser";
    }
}
