package com.flashsale.redis;

import com.flashsale.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisFactory {
    @Autowired
    private RedisConfig config;

    @Bean
    public JedisPool redisPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(config.getPoolMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(config.getPoolMaxWait());
        jedisPoolConfig.setMaxTotal(config.getPoolMaxTotal());
        jedisPoolConfig.setMinIdle(config.getPoolMinIdle());
//        jedisPoolConfig.setMinIdle(0);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,config.getHost(),
                config.getPort(),
                config.getTimeout(),
                null);

        return  jedisPool;
    }
}
