package com.pengchaoling.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pengchaoling.model.*;
import com.pengchaoling.service.*;
import com.pengchaoling.util.SnsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-18
 * description: 用户个人页控制器
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    WeiboService weiboService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    @RequestMapping(value = "/profile/{uid}", method = {RequestMethod.GET})
    public String profile(Model model, @PathVariable("uid") int uid,@RequestParam(value = "p",required = false,defaultValue ="1") int p) {
        String url = "/profile/"+ uid;
        User user = userService.getUserById(uid);
        if(user==null){
            model.addAttribute("error_msg", "非法操作，没有该用户");
            model.addAttribute("jump_url", url);
            return "dispatch_jump";
        }

        UserInfo profile = userInfoService.getUserInfoByUid(uid);
        model.addAttribute("profile",profile);
        //开始分页
        PageHelper.startPage(p, 15);

        //个人微博列表
        List<Weibo> weibos = weiboService.selectWeibosByUid(profile.getUid());
        List<ViewObject> vos = new ArrayList<ViewObject>();
        if(!weibos.isEmpty()){
            for(Weibo weibo : weibos){
                ViewObject vo = new ViewObject();
                vo.set("weibo",weibo);
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
                        vo.set("pictureTurn",weiboService.selectPictureByWid(weibo.getIsturn()));
                        vo.set("turnLikeCount", likeService.getLikeCount(EntityType.ENTITY_WEIBO, weibo.getIsturn()));
                    }
                }

                vos.add(vo);
            }
        }
        //是否已关注该用户
        boolean followed = followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,uid);
        model.addAttribute("followed",followed);

        //获取当前用户的所有粉丝  暂时没做分页
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, uid, 0, 100);
        model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));

        //获取当前用户的所有偶像个（关注对象） 暂时没做分页
        List<Integer> followeeIds = followService.getFollowees(uid, EntityType.ENTITY_USER, 0, 100);
        model.addAttribute("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
        model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));


        //微博列表分页字符串
        PageInfo page = new PageInfo(weibos);
        String pageStr = SnsUtil.showPage(page,url,"?p");
        model.addAttribute("pageStr",pageStr);
        model.addAttribute("vos",vos);
        return "profile";
    }


    /**
     *  获取用户信息 这只是个辅助方法
     */
    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            UserInfo userInfo = userInfoService.getUserInfoByUid(uid);
            if (userInfo == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("userinfo", userInfo);
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            userInfos.add(vo);
        }
        return userInfos;
    }
}
