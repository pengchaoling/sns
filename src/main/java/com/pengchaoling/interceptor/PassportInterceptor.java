package com.pengchaoling.interceptor;

import com.pengchaoling.dao.LoginTicketDAO;
import com.pengchaoling.dao.UserDAO;
import com.pengchaoling.dao.UserInfoDAO;
import com.pengchaoling.model.*;
import com.pengchaoling.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Author: Lying
 * Data: 2017-02-16
 * description: 拦截器
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserInfoDAO userInfoDAO;

    @Autowired
    FollowService followService;

    /**
     *  处理controller之前，会先调用
     *  如果用户保存有cookie,那么会从cookie里读取
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String ticket = null;

        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            //用户还没有登陆，直接返回空信息
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }

            User user = userDAO.selectById(loginTicket.getUid());
            hostHolder.setUser(user);
        }
        return true;
    }

    /**
     *  Controller页面渲染之前，会调用这个
     *  把用户信息注入到每一个将要渲染的页面
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (modelAndView != null && user != null) {
            UserInfo userInfo = userInfoDAO.selectUserInfoByUid(user.getId());
            userInfo.setFollower(followService.getFollowerCount(EntityType.ENTITY_USER, user.getId()));
            userInfo.setFollowee(followService.getFolloweeCount(user.getId(), EntityType.ENTITY_USER));
            modelAndView.addObject("user", user);
            modelAndView.addObject("userinfo",userInfo);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
