package com.jasmine.learingsb.middleware.sessionredis;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/test/session")
public class TestSessionRedisController {

    /**
     * 如果session存在name, 则返回session中的name
     * 例如：
     * 请求12345 : name=1，name不存在，则放入session，并返回1
     * 请求12346 : name=2，name存在，则返回1
     *
     *
     * @param name
     * @param request
     * @return
     */
    @GetMapping("/get")
    public String get(@RequestParam("name") String name, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (StrUtil.isBlank((String) session.getAttribute("name"))) {
            session.setAttribute("name",name);
            return "放入session ： " + name;
        }
        return "直接session ： " + session.getAttribute("name");
    }
}
