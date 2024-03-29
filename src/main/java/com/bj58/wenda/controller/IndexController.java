package com.bj58.wenda.controller;

import com.bj58.wenda.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {

    @Autowired
    WendaService wendaService;

    @RequestMapping(path={"/", "/index"})
    @ResponseBody
    public String index(HttpSession httpSession) {
        return wendaService.getMessage(2) + " Hello World! " + httpSession.getAttribute("msg");
    }

    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "star", required = false) String key) {
        return String.format("{%s} {%d} {%d} {%s}", groupId, userId, type, key);
    }

    @RequestMapping(path={"/th"}, method = {RequestMethod.GET})
    public String template(Model model) {
        model.addAttribute("value1", "vvvv1");
        List<String> colors = Arrays.asList(new String[] {"RED", "GREEN", "BLUE"});
        model.addAttribute("colors", colors);
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i*i));
        }
        model.addAttribute("map", map);
        return "home";
    }

    @RequestMapping(path={"/request"})
    @ResponseBody
    public String request(Model model, HttpServletRequest request, HttpServletResponse response,
                          HttpSession httpSession, @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE:" + sessionId);
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                sb.append("Cookie:" + cookie.getName() + " value:" + cookie.getValue() + "<br>");
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        response.addHeader("starId", "hello") ;
        response.addCookie((new Cookie("username", "star")));
        return sb.toString();
    }

    @RequestMapping(path={"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code, HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");
        RedirectView red = new RedirectView("/", true);
        if(code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
        //return "redirect:/";
    }

    @RequestMapping(path={"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error: " + e.getMessage();
    }
}
