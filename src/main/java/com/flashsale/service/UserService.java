package com.flashsale.service;

import com.flashsale.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

public interface UserService {
    int getUserTotal();
    String loginCheck(LoginVo loginVo, HttpServletResponse response);
}
