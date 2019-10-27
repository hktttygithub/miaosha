package com.flashsale.controller;

import com.flashsale.result.Result;
import com.flashsale.util.RedisUtil;
import com.flashsale.vo.LoginVo;
import com.flashsale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redis;

    @RequestMapping("/hello")
//    @ResponseBody
    public String hello(Model model) {
        model.addAttribute("total", userService.getUserTotal());
        return "helloworld";
    }

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(LoginVo loginVo, HttpServletResponse response) {
        String token = userService.loginCheck(loginVo,response);

        return Result.success(token);
    }

}
