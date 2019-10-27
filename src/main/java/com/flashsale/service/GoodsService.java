package com.flashsale.service;

import com.flashsale.pojo.User;
import com.flashsale.vo.GoodsVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface GoodsService {
    List<GoodsVo> getGoodsList();

    <T>  T getRedisObject(String tooken, HttpServletResponse response, Class<T> clazz,int expires);

    //只从redis中获取对象，不延长有效期
    <T>  T getRedisObject(String tooken, Class<T> clazz);

    GoodsVo getGoodsVoByGoodsId(long goodsId);


    GoodsVo getGoodsVO(long goodsId);

    boolean reduceStock(GoodsVo goodsVo);

    void updateCount(int count);


}
