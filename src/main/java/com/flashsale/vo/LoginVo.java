package com.flashsale.vo;

import lombok.Data;

import java.util.Objects;

@Data
public class LoginVo {
    private String mobile;
    private String password;

    //用户登陆Cookie和Redis的过期时间
    public static final int LOGINVO_EXPIRES = 3600 * 24;
    //cookie的name
    public static final String TOKEN = "token";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginVo)) return false;
        LoginVo loginVo = (LoginVo) o;
        return Objects.equals(password, loginVo.password) &&
                Objects.equals(mobile, loginVo.mobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, mobile);
    }
}
