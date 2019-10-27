package com.flashsale.service;

import com.flashsale.pojo.OrderInfo;
import com.flashsale.pojo.User;
import com.flashsale.vo.GoodsVo;

public interface MiaoshaService {
    OrderInfo miaosha(User user, GoodsVo goodsVo);
}
