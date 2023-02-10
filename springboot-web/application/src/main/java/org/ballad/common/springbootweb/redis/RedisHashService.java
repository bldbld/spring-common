package org.ballad.common.springbootweb.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class RedisHashService {


    public void redisPus(RedisTemplate redisTemplate, String key, StoObj obj) {
        RedisOperateUtil.push(redisTemplate, key, obj);
    }

    public StoObj redisGet(RedisTemplate redisTemplate, String key) {


        StoObj obj = RedisOperateUtil.get(redisTemplate, key);
        System.out.println("REDIS GET" + key + " i value " + obj.i);
        return obj;
    }


}
