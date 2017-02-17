package com.pengchaoling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Author: Lying
 * Data: 2017-02-17
 * description:公用的基础控制器，一些杂七杂八的功能写在这里
 *
 */
@Controller
public class CommontController {
    /**
     * Ajax创建新分组
     */
    @RequestMapping(value = "/Comment/addGroup", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("name") String name) {
        return "";

    }
}
