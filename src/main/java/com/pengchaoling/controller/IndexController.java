package com.pengchaoling.controller;

import com.pengchaoling.model.User;
import com.pengchaoling.model.ViewObject;
import com.pengchaoling.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Lying
 * Data: 2017-02-15
 * description: 首页
 */
@Controller
public class IndexController {
    @Autowired
    UserService UserService;



    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        return "index";
    }
}
