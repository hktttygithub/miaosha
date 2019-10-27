import com.flashsale.Application;
import com.flashsale.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ResetRedis {

    @Autowired
    RedisUtil redis;

    @Test
    public void test1(){
        redis.set("GoodsStock_goodsId1",String.valueOf(10));
    }
}