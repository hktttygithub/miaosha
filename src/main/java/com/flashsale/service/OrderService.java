package com.flashsale.service;

import com.flashsale.pojo.MiaoshaOrder;
import com.flashsale.pojo.OrderInfo;
import com.flashsale.pojo.User;
import com.flashsale.vo.GoodsVo;

public interface OrderService {
    MiaoshaOrder getByUserIdGoodsId(long userId,long goodsId);

    OrderInfo createOrder(User user, GoodsVo goodsVo);
}
