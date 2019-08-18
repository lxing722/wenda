package com.bj58.wenda.service;

import com.bj58.wenda.dao.LoginTicketDAO;
import com.bj58.wenda.dao.UserDAO;
import com.bj58.wenda.model.LoginTicket;
import com.bj58.wenda.model.User;
import com.bj58.wenda.util.WendaUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int userId) {
        return userDAO.selectById(userId);
    }

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();

        if(StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if(StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if(user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        map.put("userId", user.getId());
        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        loginTicket.setExpired(date);
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }
    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }

    public int userCount() {
        return userDAO.userCount();
    }

    public int addUser(User user) {
        return userDAO.addUser(user);
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
