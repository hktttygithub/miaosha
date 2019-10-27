package com.flashsale.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flashsale.pojo.MiaoshaOrder;
import com.flashsale.pojo.OrderInfo;
import com.flashsale.pojo.User;
import com.flashsale.result.CodeMsg;
import com.flashsale.service.GoodsService;
import com.flashsale.service.MiaoshaService;
import com.flashsale.service.OrderService;
import com.flashsale.service.ThreadPoolService;
import com.flashsale.util.RedisUtil;
import com.flashsale.util.ThreadPoolUtil;
import com.flashsale.vo.GoodsVo;
import com.flashsale.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Controller
@RequestMapping("/miaosha")
@Slf4j
public class MiaoShaController implements InitializingBean, ApplicationContextAware {
    @Autowired
    RedisUtil redis;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    ThreadPoolUtil threadPool;
//    @Autowired
//    ThreadPoolService threadPoolService;

    ApplicationContext applicationContext;

    Lock lock = new ReentrantLock();


    //库存数
    public static volatile boolean over = false;

    @Override
    public void afterPropertiesSet() throws Exception {
        GoodsVo goodsVO = goodsService.getGoodsVO(1);
        redis.set("GoodsStock_goodsId1",""+ goodsVO.getStockCount());
        redis.set("Goods_goodsId1", JSON.toJSONString(goodsVO));
    }

    @RequestMapping("/reset")
    @ResponseBody
    public String reset(){
        redis.set("GoodsStock_goodsId1",String.valueOf(10));
        goodsService.updateCount(10);

        over=false;
        return "重置";
    }




    @RequestMapping("/do_miaosha")
    public String list(Model model,
                       @CookieValue(value = LoginVo.TOKEN, required = false) String token,
                       @RequestParam(required = false) String token2,
                       HttpServletResponse response,
                       @RequestParam("goodsId") long goodsId) {

        if (token == null && token2 == null) {
            return "login";
        }
        if (token == null) {
            token = token2;
        }
        //减少对redis的访问
        if(over){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
//        int count = (int)redis.sadd("miaoshaUserToken", token);
//        if(count==0){ // 重复秒杀
//            System.out.println(token);
//            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
//            return "miaosha_fail";
//        }
        //从redis中获取库存数
        int stock = (int)redis.decr("GoodsStock_goodsId" + goodsId);
        //库存为10时，放11个线程进入线程池
        //小于-1时直接返回商品秒杀结束
        if (stock < -1) {
            over = true;
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //初始化线程池类的秒杀所需要的参数
        User user = goodsService.getRedisObject(token, response, User.class, LoginVo.LOGINVO_EXPIRES);
        ThreadPoolService threadPoolService = getThreadPooolServiceBean();
        threadPoolService.setGoodsId(goodsId);
        threadPoolService.setUser(user);
        //交给线程池，异步下单
        Executor pool = threadPool.getPool();
        //threadPoolService是多例的
        pool.execute(threadPoolService);

        model.addAttribute("orderInfo", new OrderInfo());
        model.addAttribute("goods", new GoodsVo());

        return "order_detail";

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext =applicationContext;
    }

    private ThreadPoolService getThreadPooolServiceBean(){
        ThreadPoolService threadPoolService =
                (ThreadPoolService) applicationContext.getBean("threadPoolService");
        return threadPoolService;
    }
}
