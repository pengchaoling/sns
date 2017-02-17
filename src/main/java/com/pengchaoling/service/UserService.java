package com.pengchaoling.service;

import com.pengchaoling.dao.UserDAO;
import com.pengchaoling.dao.LoginTicketDAO;
import com.pengchaoling.model.LoginTicket;
import com.pengchaoling.model.User;
import com.pengchaoling.model.UserInfo;
import com.pengchaoling.util.SnsUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author: Lying
 * Data: 2017-02-15
 * description: UserService
 */
@Service
public class UserService {
    //通过以来注入方式来生成对象,不需要每次使用都new
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Autowired
    private UserInfoService userInfoService;

    public User getUserById(int id) {
        return userDAO.selectById(id);
    }

    public User getUserByAccount(String account){
        return userDAO.selectByAccount(account);
    }

    public User getUserByNickname(String nickname){
        return userDAO.selectByNickname(nickname);
    }

    public List<User> getUsers(int offset, int limit){
        return userDAO.selectUsers(offset, limit);
    }

    /**
     * 注册验证以及注册完成
     */
    public Map<String, Object> register(String account,String nickname, String pwd,String pwded) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(account)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(nickname)) {
            map.put("msg", "昵称不能为空");
            return map;
        }

        if (StringUtils.isBlank(pwd)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        if(!pwd.equals(pwded)){
            map.put("msg","两次输入密码不一样");
            return map;
        }

        User user = userDAO.selectByAccount(account);

        if (user != null) {
            map.put("msg", "该账户已被注册");
            return map;
        }

        User nick = userDAO.selectByNickname(nickname);
        if(nick != null){
            map.put("msg","该昵称已被人使用，换一个吧");
        }

        // 设置用户信息，密码加盐
        user = new User();
        user.setAccount(account);
        user.setNickname(nickname);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(SnsUtil.MD5(pwd+user.getSalt()));
        Date date = new Date();
        //date.setTime(date.getTime()+3600);
        user.setRegistime(date);
        userDAO.addUser(user);

        //设置useInfo
        userInfoService.addDefaultUserInfo(nickname,user.getId());

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }
    /**
     * 登录处理
     */
    public Map<String, Object> login(String account, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(account)) {
            map.put("msg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByAccount(account);

        if (user == null) {
            map.put("msg", "账号不存在");
            return map;
        }

        if (!SnsUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }
    /**
     * 登录ticket入库
     */
    private String addLoginTicket(int uid) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUid(uid);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
    /**
     * 手动安全退出登陆，修改ticket的statue，ticket将失效，下次登陆会重新生成
     */
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
