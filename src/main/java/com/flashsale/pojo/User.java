package com.flashsale.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    //id是主键，用户的手机号码作为id
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
