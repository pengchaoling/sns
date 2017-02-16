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
        List<String> arr = new ArrayList<String>();
        arr.add("A");
        arr.add("B");
        arr.add("C");

       List<User> users= UserService.getUsers(0,10);

        User user = UserService.getUserById(2);

        //User user = UserService.getUserByAccount("lovepcl");

        model.addAttribute("us",users);

        //model.addAttribute("userss",user);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);


        model.addAttribute("arr",arr);
        model.addAttribute("name","pengchaoling");
        model.addAttribute("user", new User("pengchag"));
        return "index";
    }
}
