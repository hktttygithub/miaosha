package com.flashsale.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flashsale.pojo.User;
import com.flashsale.service.GoodsService;
import com.flashsale.util.RedisUtil;
import com.flashsale.vo.GoodsVo;
import com.flashsale.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisUtil redis;
/*

model.addAttribute("user", user);
    	//取缓存
    	String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
    	if(!StringUtils.isEmpty(html)) {
				return html;
    	}
    	List<GoodsVo> goodsList = goodsService.listGoodsVo();
    	model.addAttribute("goodsList", goodsList);
//    	 return "goods_list";
    	SpringWebContext ctx = new SpringWebContext(request,response,
    			request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
    	//手动渲染
    	html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
    	if(!StringUtils.isEmpty(html)) {
    		redisService.set(GoodsKey.getGoodsList, "", html);
    	}
    	return html;

    	*/
    @RequestMapping("/to_list")
    public String list(Model model,
                       @CookieValue(value = LoginVo.TOKEN,required = false)  String token,
                       @RequestParam(required =false) String token2,
                       HttpServletResponse response) {
        if(token==null && token2==null) {
            return "login";
        }
        if(token==null){
            token = token2;
        }
        List<GoodsVo> goodsList =null;
        //根据token从redis中取user对象
        User user = goodsService.getRedisObject(token,response,User.class,LoginVo.LOGINVO_EXPIRES);
        String goodsJSON = redis.get(GoodsVo.GOODSLIST);
        if(goodsJSON!=null){
            goodsList = JSONObject.parseArray(goodsJSON, GoodsVo.class);
        }else{
            goodsList = goodsService.getGoodsList();
            String rdisValue = JSON.toJSONString(goodsList);
            redis.setex(GoodsVo.GOODSLIST,GoodsVo.GoodsVo_EXPIRES,rdisValue);
            log.info("访问了数据库");
        }
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("user",user);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model,
                         @CookieValue(value = LoginVo.TOKEN,required = false)  String token,
                         @RequestParam(required =false) String token2,
                         @PathVariable("goodsId")long goodsId,
                         HttpServletResponse response) {
        if(token==null && token2==null) {
            System.out.println("1");
            return "login";
        }
        if(token==null){
            token = token2;
        }
        //根据token从redis中取user对象
        User user = goodsService.getRedisObject(token,response,User.class,LoginVo.LOGINVO_EXPIRES);
        model.addAttribute("user", user);
        GoodsVo goods = null;
        String jsonStr = redis.get(GoodsVo.GOODSDETAIL);
        if(jsonStr!=null) {
            goods = JSONObject.parseObject(jsonStr,GoodsVo.class);
        }else {
            goods = goodsService.getGoodsVoByGoodsId(goodsId);
            redis.setex(GoodsVo.GOODSDETAIL, GoodsVo.GoodsVo_EXPIRES, JSON.toJSONString(goods));
            log.info("to_detail_访问了数据库");
        }
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

}
