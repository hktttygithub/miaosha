package com.flashsale.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flashsale.excception.ForwardException;
import com.flashsale.excception.GlobalException;
import com.flashsale.mapper.GoodsMapper;
import com.flashsale.pojo.Goods;
import com.flashsale.pojo.MiaoshaGoods;
import com.flashsale.result.CodeMsg;
import com.flashsale.util.RedisUtil;
import com.flashsale.vo.GoodsVo;
import com.flashsale.vo.LoginVo;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@Transactional
@Slf4j
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    RedisUtil redis;

    @Override
    public List<GoodsVo> getGoodsList() {
        List<GoodsVo> list = goodsMapper.getList();
        return list;
    }

    @Override
    //根据cookie中的值，从redis中取value，并延长cookie和redis的有效期
    public  <T>  T getRedisObject(String tooken, HttpServletResponse response, Class<T> clazz,int expires) {
        String jsonString = redis.get(tooken);
        redis.setex(tooken,expires,jsonString);
        T t = JSON.toJavaObject(JSON.parseObject(jsonString), clazz);
        Cookie cookie = new Cookie(LoginVo.TOKEN,tooken);
        cookie.setPath("/");
        cookie.setMaxAge(expires);
        response.addCookie(cookie);
        return t;
    }

    @Override
    public <T> T getRedisObject(String tooken, Class<T> clazz) {
        String jsonString = redis.get(tooken);
        T t = JSON.toJavaObject(JSON.parseObject(jsonString), clazz);
        return t;
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        GoodsVo goods = goodsMapper.getById(goodsId);
        return goods;
    }

    @Override
    public GoodsVo getGoodsVO(long goodsId) {
        GoodsVo goods = goodsMapper.getById(goodsId);
        return goods;
    }

    @Override
    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int flag = goodsMapper.reduceStock(g);
        if(flag!=1){

//            throw new ForwardException(CodeMsg.MIAO_SHA_OVER);
            return false;
        }
        return true;
    }

    @Override
    public void updateCount(int count) {
        goodsMapper.updateCount(count);
    }

}
