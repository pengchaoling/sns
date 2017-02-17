package com.pengchaoling.controller;

import com.pengchaoling.model.HostHolder;
import com.pengchaoling.model.User;
import com.pengchaoling.model.UserInfo;
import com.pengchaoling.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Author: Lying
 * Data: 2017-02-17
 * description: 用户个人信息设置
 */
@Controller
public class UserSettingController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 用户信息设置页面
     */
    @RequestMapping(path = {"/userSetting"}, method = {RequestMethod.GET})
    public String userSetting(Model model) {

        return "userSetting";
    }
}
