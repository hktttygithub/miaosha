package com.flashsale.mapper;

import com.flashsale.pojo.Goods;
import com.flashsale.pojo.MiaoshaGoods;
import com.flashsale.vo.GoodsVo;

import java.util.List;

public interface GoodsMapper {
    List<GoodsVo> getList();

    GoodsVo getById(long goodsId);

    int reduceStock(MiaoshaGoods g);

    void updateCount(int count);
}
