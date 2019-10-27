package com.flashsale.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisUtil {
    @Autowired
    JedisPool jedisPool;

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String s = jedis.get(key);
            return s;
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean set(String key, String value) {
        if (value == null || key == null) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean setex(String key, int seconds, String value) {
        if (value == null || key == null) {
            return false;
        }
        if (seconds < 0) {
            seconds = 0;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(key, seconds, value);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 判断key是否存在
     */
    public <T> boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } finally {
            returnToPool(jedis);
        }
    }

    //减一并返回
    public long decr(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return  jedis.decr(key);
        }finally {
            returnToPool(jedis);
        }

    }

    //加1然后返回
    public long incr(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return  jedis.incr(key);
        }finally {
            returnToPool(jedis);
        }

    }

    public long sadd(String key,String setValue){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sadd(key, setValue);
        }finally {
            returnToPool(jedis);
        }

    }
}
