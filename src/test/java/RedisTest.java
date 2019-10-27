import com.alibaba.fastjson.JSON;
import com.flashsale.Application;
import com.flashsale.controller.MiaoShaController;
import com.flashsale.pojo.User;
import com.flashsale.service.GoodsService;
import com.flashsale.util.RedisUtil;
import com.flashsale.vo.GoodsVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {
    @Autowired
    RedisUtil redisUtil;

//    @Test
    public void test1() {
        User user = new User();
        user.setId(34124444L);
        user.setHead("ssdd");
        user.setLastLoginDate(new Date());
        user.setRegisterDate(new Date());
        user.setNickname("hk");
        user.setSalt("2009");
        String s = JSON.toJSONString(user);
        //尝试添加10k个key到redis，看内存占用
        for (long i = 20000000; i < 100000000; i++) {
            redisUtil.set("key" + i, s);
        }
    }
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisUtil redis;

    @Test
    public void reset(){
        GoodsVo goodsVO = goodsService.getGoodsVO(1);
        redis.set("GoodsStock_goodsId1",""+ goodsVO.getStockCount());
        MiaoShaController.over = false;
        redis.set("Goods_goodsId1", JSON.toJSONString(goodsVO));
    }
}