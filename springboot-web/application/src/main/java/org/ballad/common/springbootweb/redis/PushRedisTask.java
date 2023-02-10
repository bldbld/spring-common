package org.ballad.common.springbootweb.redis;

import org.springframework.data.redis.core.RedisTemplate;


import java.util.concurrent.RecursiveTask;

public class PushRedisTask extends RecursiveTask<StoObj> {


    private String key;

    private StoObj obj;

    private RedisTemplate redisTemplate;

    public PushRedisTask(String key, StoObj obj, RedisTemplate redisTemplate) {
        this.key = key;
        this.obj = obj;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected StoObj compute() {
        RedisOperateUtil.addOne(redisTemplate, key);
        return null;
    }
}
