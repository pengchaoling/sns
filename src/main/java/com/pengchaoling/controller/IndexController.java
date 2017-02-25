package com.pengchaoling.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pengchaoling.model.*;
import com.pengchaoling.service.*;
import com.pengchaoling.util.JedisAdapter;
import com.pengchaoling.util.RedisKeyUtil;
import com.pengchaoling.util.SnsUtil;
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
    @Autowired
    LikeService likeService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    FeedService feedService;


    /**
     * 首页 个人时间线
     */
    @RequestMapping(path = {"/","/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,@RequestParam(value = "p",required = false,defaultValue ="1") int p) {
        //获取用户分组
        List<FocusGroup> focusGroups = focusGroupService.selectFocusGroupsByUid(hostHolder.getUser().getId());

        //从redis取出当前登陆用户的 feed 时间线了其实
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(hostHolder.getUser().getId()), 0, 15);
        List<Feed> feeds = new ArrayList<Feed>();
        //再从数据库里面读取出来
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }

        List<ViewObject> vos = new ArrayList<ViewObject>();

        for(Feed oneFeed:feeds){
            ViewObject vo = new ViewObject();
            //时间线的最顶部，谁，干了什么事情
            vo.set("ownerInfo",userInfoService.getUserInfoByUid(oneFeed.getUid()));
            vo.set("message",oneFeed.get("message"));
            vo.set("feedTime",oneFeed.getCreatedDate());

            //如果是微博类型或者点赞类型 feed
            if(oneFeed.getEntityType()==EntityType.ENTITY_WEIBO){
                Weibo weibo = weiboService.selectWeiboById(oneFeed.getEntityId());
                if(weibo == null){
                    continue;
                }
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
                        vo.set("turnLikeCount", likeService.getLikeCount(EntityType.ENTITY_WEIBO, weibo.getIsturn()));

                        vo.set("pictureTurn",weiboService.selectPictureByWid(weibo.getIsturn()));
                    }
                }

                vos.add(vo);
            }
        }

        model.addAttribute("vos",vos);
        return "index";
    }

    /**
     *  广场，所有人的微博
     */
    @RequestMapping(path = {"/square",}, method = {RequestMethod.GET, RequestMethod.POST})
    public String square(Model model,@RequestParam(value = "p",required = false,defaultValue ="1") int p) {
        //获取用户分组
        List<FocusGroup> focusGroups = focusGroupService.selectFocusGroupsByUid(hostHolder.getUser().getId());
        //开始分页
        PageHelper.startPage(p, 15);
        List<Weibo> weibos = weiboService.selectWeibos();
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
                        vo.set("turnLikeCount", likeService.getLikeCount(EntityType.ENTITY_WEIBO, weibo.getIsturn()));

                        vo.set("pictureTurn",weiboService.selectPictureByWid(weibo.getIsturn()));
                    }
                }

                vos.add(vo);
            }
        }
        //分页字符串
        PageInfo page = new PageInfo(weibos);
        String pageStr = SnsUtil.showPage(page,"/square","?p");
        model.addAttribute("pageStr",pageStr);
        model.addAttribute("vos",vos);
        model.addAttribute("focusGroups",focusGroups);
        return "square";
    }

}
