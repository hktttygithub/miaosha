package com.flashsale.service;

import com.alibaba.fastjson.JSONObject;
import com.flashsale.controller.MiaoShaController;
import com.flashsale.pojo.MiaoshaOrder;
import com.flashsale.pojo.OrderInfo;
import com.flashsale.pojo.User;
import com.flashsale.result.CodeMsg;
import com.flashsale.util.RedisUtil;
import com.flashsale.vo.GoodsVo;
import com.flashsale.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Scope(value = "prototype")
@Service
public class ThreadPoolService implements Runnable{
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisUtil redis;
    @Autowired
    MiaoshaService miaoshaService;

//    public volatile static Long date = null;
//    Lock lock = new ReentrantLock();

    private User user;
    private long goodsId;

    public void setUser(User user) {
        this.user = user;
    }
    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    AtomicInteger integer = new AtomicInteger();
    ConcurrentHashMap<ThreadPoolService,Integer> map = new ConcurrentHashMap<>(32);

    @Override
    @Transactional
    public void run() {
//        if(date==null){
//            lock.lock();
//            try {
//                if(date==null){
//                    date =System.currentTimeMillis();
//                    System.out.println(date);
//                }
//            }finally {
//                lock.unlock();
//            }
//        }
        GoodsVo goodsVo  = JSONObject.parseObject( redis.get("Goods_goodsId" + goodsId)  ,GoodsVo.class);
        if(goodsVo==null){
            GoodsVo goodsVO = goodsService.getGoodsVO(goodsId);
        }
//        goodsVo.setStockCount(stock);
//        判断用户是否已经秒杀过该商品
//        MiaoshaOrder order = orderService.getByUserIdGoodsId(user.getId(), goodsId);
//        if (order != null) {
////            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
////            return "miaosha_fail";
//            System.out.println("重复秒杀");
//            MiaoShaController.over = false;
//            redis.incr("GoodsStock_goodsId" +1);
//            return;
//        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);

//        model.addAttribute("orderInfo", orderInfo);
//        model.addAttribute("goods", goodsVo);
    }


}
