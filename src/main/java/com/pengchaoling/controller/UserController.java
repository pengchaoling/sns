package com.pengchaoling.controller;

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
    @RequestMapping(value = "/profile/{uid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("uid") int uid) {

        return "profile";
    }
}
