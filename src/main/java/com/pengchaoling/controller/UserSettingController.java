package com.pengchaoling.controller;

import com.pengchaoling.model.HostHolder;
import com.pengchaoling.model.UserInfo;
import com.pengchaoling.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Lying
 * Data: 2017-02-17
 * description: 用户个人信息设置
 */
@Controller
public class UserSettingController {
    private static final Logger logger = LoggerFactory.getLogger(UserSettingController.class);
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    HostHolder hostHolder;


    /**
     * 用户信息设置页面
     */
    @RequestMapping(path = {"/userSetting"}, method = {RequestMethod.GET})
    public String userSetting(Model model) {

        return "UserSetting";
    }
    /**
     * 修改头像表单处理
     */
    @RequestMapping(value = "/editFace", method = {RequestMethod.POST})
    public String editFace(Model model,@RequestParam("face50") String face50,
                           @RequestParam("face80") String face80,
                           @RequestParam("face180") String face180,
                           HttpServletResponse response) {

        try {
            userInfoService.updateFace(face50,face80,face180,hostHolder.getUser().getId());
            model.addAttribute("success_msg", "修改头像成功");
            model.addAttribute("jump_url", "/userSetting");
            return "dispatch_jump";

        } catch (Exception e) {
            logger.error("修改头像异常", e.getMessage());
            model.addAttribute("error_msg", "修改头像失败");
            model.addAttribute("jump_url", "/userSetting");
            return "dispatch_jump";
        }

    }

    /**
     * 修改基本信息
     */
    @RequestMapping(value = "/editBasic", method = {RequestMethod.POST})
    public String editBasci(Model model,@RequestParam("nickname") String nickname,
                            @RequestParam("truename") String truename,
                            @RequestParam("sex") int  sex,
                            @RequestParam("province") String province,
                            @RequestParam("city") String city,
                            @RequestParam("night") String night,
                            @RequestParam("intro") String intro,
                           HttpServletResponse response) {


        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUid(hostHolder.getUser().getId());
            userInfo.setNickname(nickname);
            userInfo.setTruename(truename);
            userInfo.setSex(sex);
            userInfo.setLocation(province+" "+city);
            //星座
            userInfo.setConstellation(night);
            userInfo.setIntro(intro);
            userInfoService.updateBasic(userInfo);


            model.addAttribute("success_msg", "修改信息成功");
            model.addAttribute("jump_url", "/userSetting");
            return "dispatch_jump";

        } catch (Exception e) {
            logger.error("修改信息异常", e.getMessage());
            model.addAttribute("error_msg", "修改信息失败");
            model.addAttribute("jump_url", "/userSetting");
            return "dispatch_jump";
        }

    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/editPwd", method = {RequestMethod.POST})
    public String editPwd(Model model) {
            model.addAttribute("success_msg", "对不起，次功能正在开发中......");
            model.addAttribute("jump_url", "/userSetting");
            return "dispatch_jump";
    }

}
