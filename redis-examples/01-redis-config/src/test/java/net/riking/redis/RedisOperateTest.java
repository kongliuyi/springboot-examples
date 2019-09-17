package net.riking.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Description
 * @Author kongLiuYi
 * @Date 2019/9/17 0017 18:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisOperateTest {
    @Autowired
    RedisOperate redisOperate;

    @Test
    public void  valueOperationsTest() throws InterruptedException {
        redisOperate.valueOperations();
    }

}