package com.pengchaoling.controller;

import com.pengchaoling.model.FocusGroup;
import com.pengchaoling.model.HostHolder;
import com.pengchaoling.model.ViewObject;
import com.pengchaoling.model.Weibo;
import com.pengchaoling.service.FocusGroupService;
import com.pengchaoling.service.UserInfoService;
import com.pengchaoling.service.UserService;
import com.pengchaoling.service.WeiboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-15
 * description: 首页
 */
@Controller
public class IndexController {
    @Autowired
    UserService UserService;

    @Autowired
    FocusGroupService focusGroupService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    WeiboService weiboService;

    @Autowired
    UserInfoService userInfoService;


    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        //获取用户分组
        List<FocusGroup> focusGroups = focusGroupService.selectFocusGroupsByUid(hostHolder.getUser().getId());

        List<Weibo> weibos = weiboService.selectWeibos(0,100);
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
        model.addAttribute("focusGroups",focusGroups);
        return "index";
    }
}
