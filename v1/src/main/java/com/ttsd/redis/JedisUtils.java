package com.ttsd.redis;

import com.ttsd.redis.JedisTemplate.JedisAction;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class JedisUtils {

    /**
     * 在Pool以外强行销毁Jedis.
     */
    public static void destroyJedis(Jedis jedis) {
        if ((jedis != null) && jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                } catch (Exception e) {
                }
                jedis.disconnect();
            } catch (Exception e) {
            }
        }
    }


}
