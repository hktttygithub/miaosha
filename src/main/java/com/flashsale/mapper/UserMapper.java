package com.flashsale.mapper;

import com.flashsale.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


public interface UserMapper {
    public int getTotal();

    User getUserById(@Param("id") Long mobile);
}
