package org.ballad.common.springbootweb.sample.mybatisplus.sample;

import org.ballad.common.springbootweb.StartApplication;
import org.ballad.common.springbootweb.redis.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = StartApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private RedisHashService redisHashService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testSelect() {

        for (int i = 0; i < 5000; i++) {
            StoObj obj = new StoObj(String.valueOf(i), Double.valueOf(i));
            redisHashService.redisPus(redisTemplate, String.valueOf(i), obj);
        }
        for (int i = 0; i < 5000; i++) {
            redisHashService.redisGet(redisTemplate, String.valueOf(i));
        }
    }

    @Test
    public void testSelect2() {

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());

        for (int i = 0; i <= 1; i++) {
            String key = String.valueOf(i);
            StoObj obj = new StoObj(String.valueOf(i), 0.0d);
            RedisOperateUtil.push(redisTemplate, key, obj);

            for (int j = 0; j <= 100; j++) {
                Thread thread = new Thread(() -> {
                    RedisOperateUtil.addOne(redisTemplate, key);
                });
                thread.start();
            }
        }
        for (int i = 0; i <= 1; i++) {
            String key = String.valueOf(i);
            StoObj obj = RedisOperateUtil.get(redisTemplate, key);
            System.out.println("REDIS GET" + key + " i value " + obj.getI());
        }
    }

}
