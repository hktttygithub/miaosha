package com.flashsale.mapper;

import com.flashsale.pojo.MiaoshaOrder;
import com.flashsale.pojo.OrderInfo;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    MiaoshaOrder getByUserIdGoodsId(@Param("userId") long userId,@Param("goodsId") long goodsId);

    public long insert(OrderInfo orderInfo);

    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
