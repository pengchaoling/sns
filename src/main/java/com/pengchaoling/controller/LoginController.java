package com.pengchaoling.controller;

import com.pengchaoling.model.User;
import com.pengchaoling.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Lying
 * Data: 2017-02-15
 * description: 用户注册登录控制器
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;
    /**
     * 注册页面
     */
    @RequestMapping(path = {"/register"}, method = {RequestMethod.GET})
    public String registerPage(Model model) {
        return "register";
    }

    /**
     * 注册表单处理
     */
    @RequestMapping(path = {"/doRegister/"},method = {RequestMethod.POST})
    public String doRegister(Model model, @RequestParam("account") String account,
                      @RequestParam("pwd") String pwd,
                      @RequestParam("pwded") String pwded,
                      @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                             HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(account, pwd,pwded);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");

                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                /*
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }*/
                return "redirect:/";
            } else {
                //验证错误，跳转到错误提示页面，然后重新跳回login页面
                model.addAttribute("error_msg", map.get("msg"));
                model.addAttribute("jump_url", "/login");
                return "dispatch_jump";
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            return "login";
        }
    }

    /**
     * 登录页面
     */
    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET})
    public String loginPage(Model model) {
        return "login";
    }
    /**
     * 登录表单处理
     */
    @RequestMapping(path = {"/doLogin/"}, method = {RequestMethod.POST})
    public String doLogin(Model model, @RequestParam("account") String account,
                        @RequestParam("password") String password,
                        @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(account, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                /*跳转回登录前的页面
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }*/
                return "redirect:/";
            } else {
                model.addAttribute("error_msg", map.get("msg"));
                model.addAttribute("jump_url", "/login");
                return "dispatch_jump";
            }

        } catch (Exception e) {
            logger.error("登陆异常" + e.getMessage());
            return "login";
        }
    }
    /**
     * 处理ajax验证account是否存在
     */
    @RequestMapping(path = {"/checkAccount/"},method = {RequestMethod.POST})
    @ResponseBody
    public String checkAccount(@RequestParam("account") String account,
                             HttpServletResponse response) {
        //如果账号已存在，返回false
        User user = userService.getUserByAccount(account);
        String res;
        if(user == null)
            res = "true";
        else
            res = "false";

        return res;
    }
    /**
     * 退出登陆
     */
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }



}
