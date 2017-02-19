package com.pengchaoling.controller;

import com.pengchaoling.model.HostHolder;
import com.pengchaoling.model.Picture;
import com.pengchaoling.model.UserInfo;
import com.pengchaoling.model.Weibo;
import com.pengchaoling.service.UserInfoService;
import com.pengchaoling.service.WeiboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description: 微博处理控制器，发布，转发，删除 等等操作
 */
@Controller
public class WeiboController {
    private static final Logger logger = LoggerFactory.getLogger(WeiboController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    WeiboService weiboService;

    /**
     *  微博发布处理
     */
    @RequestMapping(value = "/weiboSend", method = {RequestMethod.POST})
    public String weiboSend(Model model, @RequestParam("content") String content,
                            @RequestParam("mini") String mini,
                            @RequestParam("medium") String medium,
                            @RequestParam("max") String max,
                           HttpServletResponse response) {

        try {
            Weibo weibo = new Weibo();
            weibo.setUid(hostHolder.getUser().getId());
            weibo.setTime(new Date());
            weibo.setContent(content);
            int wid = weiboService.addWeibo(weibo);
            if(wid>0){
                //图片插入
                if(!max.isEmpty()){
                    Picture picture = new Picture();
                    picture.setMini(HtmlUtils.htmlEscape(mini));
                    picture.setMedium(HtmlUtils.htmlEscape(medium));
                    picture.setMax(HtmlUtils.htmlEscape(max));
                    picture.setWid(weibo.getId());
                    weiboService.addPicture(picture);
                }

                //微博数加一
                userInfoService.IncWeibo(hostHolder.getUser().getId());

                model.addAttribute("success_msg", "微博发布成功");
                model.addAttribute("jump_url", "/");
                return "dispatch_jump";

            }else{
                model.addAttribute("error_msg", "微博发布失败");
                model.addAttribute("jump_url", "/");
                return "dispatch_jump";
            }

        } catch (Exception e) {
            logger.error("微博发布异常", e.getMessage());
            model.addAttribute("error_msg", "微博发布失败");
            model.addAttribute("jump_url", "/");
            return "dispatch_jump";
        }

    }


}
