package com.flashsale.service;

import com.alibaba.fastjson.JSON;
import com.flashsale.excception.GlobalException;
import com.flashsale.mapper.UserMapper;
import com.flashsale.pojo.User;
import com.flashsale.util.MD5Util;
import com.flashsale.util.RedisUtil;
import com.flashsale.util.UUIDUtil;
import com.flashsale.vo.LoginVo;
import com.flashsale.result.CodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.flashsale.vo.LoginVo.LOGINVO_EXPIRES;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redis;

    @Override
    public int getUserTotal() {
        int total = userMapper.getTotal();
        return total;
    }

    @Override
    public String loginCheck(LoginVo loginVo, HttpServletResponse response) {
        if(loginVo==null){
            throw  new GlobalException(CodeMsg.SERVER_ERROR);
        }
        Long mobile;
        //检查输入是否合法
        try {
            mobile = Long.valueOf(loginVo.getMobile());
        }catch (Exception e){
            throw  new GlobalException(CodeMsg.MOBILE_ERROR);
        }
        //检查用户是否存在
        User user = getById(mobile);
        if(user==null){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String salt = user.getSalt();
        String password = user.getPassword();
        String inputPassword = loginVo.getPassword();
        //将用户传递的密码再进行一次MD5加密，比较是否与数据库存储的密码相同
        String s = MD5Util.formPassToDBPass(inputPassword, salt);
        if(!s.equals(password)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //登陆成功，添加Cookie和redis

        return addCookieAndRedis(response,user);
    }

    private String addCookieAndRedis(HttpServletResponse response,User user) {
        String uuid = UUIDUtil.uuid();
        redis.setex(uuid,LOGINVO_EXPIRES, JSON.toJSONString(user));
        Cookie cookie = new Cookie(LoginVo.TOKEN,uuid);
        cookie.setMaxAge(LOGINVO_EXPIRES);
        cookie.setPath("/");
        response.addCookie(cookie);
        return uuid;
    }

    public User getById(Long mobile) {
        return userMapper.getUserById(mobile);
    }

}
