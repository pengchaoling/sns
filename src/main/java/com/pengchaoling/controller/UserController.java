package com.pengchaoling.controller;

import com.pengchaoling.model.*;
import com.pengchaoling.service.UserInfoService;
import com.pengchaoling.service.UserService;
import com.pengchaoling.service.WeiboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "/profile/{uid}", method = {RequestMethod.GET})
    public String profile(Model model, @PathVariable("uid") int uid) {
        User user = userService.getUserById(uid);
        if(user==null){
            model.addAttribute("error_msg", "非法操作，没有该用户");
            model.addAttribute("jump_url", "/");
            return "dispatch_jump";
        }

        UserInfo profile = userInfoService.getUserInfoByUid(uid);
        model.addAttribute("profile",profile);

        List<Weibo> weibos = weiboService.selectWeibosByUid(profile.getUid(),0,100);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        if(!weibos.isEmpty()){
            for(Weibo weibo : weibos){
                ViewObject vo = new ViewObject();
                vo.set("weibo",weibo);
                vo.set("userinfo", userInfoService.getUserInfoByUid(weibo.getUid()));
                vo.set("picture",weiboService.selectPictureByWid(weibo.getId()));
                //如果是转发的，则获取原微博
                if(weibo.getIsturn()>0){
                    vo.set("weiboTurn",weiboService.selectWeiboById(weibo.getIsturn()));
                    if(weiboService.selectWeiboById(weibo.getIsturn())!=null){
                        vo.set("userTurn",userInfoService.getUserInfoByUid(weiboService.selectWeiboById(weibo.getIsturn()).getUid()));
                        vo.set("pictureTurn",weiboService.selectPictureByWid(weibo.getIsturn()));
                    }
                }

                vos.add(vo);
            }
        }

        model.addAttribute("vos",vos);


        return "profile";
    }
}
