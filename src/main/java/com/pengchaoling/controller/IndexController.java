package com.pengchaoling.controller;

import com.pengchaoling.model.FocusGroup;
import com.pengchaoling.model.HostHolder;
import com.pengchaoling.service.FocusGroupService;
import com.pengchaoling.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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


    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        //获取用户分组
        List<FocusGroup> focusGroups = focusGroupService.selectFocusGroupsByUid(hostHolder.getUser().getId());

        model.addAttribute("focusGroups",focusGroups);
        return "index";
    }
}
