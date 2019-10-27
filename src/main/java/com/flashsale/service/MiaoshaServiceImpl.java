package com.flashsale.service;

import com.flashsale.pojo.OrderInfo;
import com.flashsale.pojo.User;
import com.flashsale.util.RedisUtil;
import com.flashsale.vo.GoodsVo;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MiaoshaServiceImpl implements MiaoshaService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisUtil redis;

    @Override
    public OrderInfo miaosha(User user, GoodsVo goodsVo) {
        //减库存 下订单 写入秒杀订单
        boolean b = goodsService.reduceStock(goodsVo);
        if(!b){
            log.info("更新失败");
//            System.out.println(System.currentTimeMillis());
            //最后一个线程会插入失败，在redis中设置秒杀结束的flag
            redis.setex("MiaoshaOver",60*10,"true");
//            ThreadPoolService.date = null;
        }
        //order_info maiosha_order
        return orderService.createOrder(user, goodsVo);
    }

}
