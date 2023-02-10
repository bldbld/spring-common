package org.ballad.common.springbootweb.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisOperateUtil {
    public static void push(RedisTemplate redisTemplate, String key, StoObj obj){
        HashOperations<String, String, Double> ops = redisTemplate.opsForHash();
        ops.put(key, RedisOperateConst.REDIS_SUB_KEY_CNT, 0.1d);
    }

    public static void addOne(RedisTemplate redisTemplate, String key){
        HashOperations<String, String, Double> ops = redisTemplate.opsForHash();
        Double o = ops.get(key,RedisOperateConst.REDIS_SUB_KEY_CNT);
        System.out.println("get value : "+String.valueOf(o));
        ops.increment(key, RedisOperateConst.REDIS_SUB_KEY_CNT, 1.0d);
    }

    public static StoObj get(RedisTemplate redisTemplate, String key) {
        HashOperations<String, String, Double> ops = redisTemplate.opsForHash();
        Double i = ops.get(key, RedisOperateConst.REDIS_SUB_KEY_CNT);
        StoObj obj = new StoObj(null, (Double) i);
        return obj;
    }
}
