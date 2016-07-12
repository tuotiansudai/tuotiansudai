package com.tuotiansudai.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class AppTokenRedisWrapperClient extends AbstractRedisWrapperClient {

    static Logger logger = Logger.getLogger(AppTokenRedisWrapperClient.class);

    @Value("${common.token.cache.db}")
    private int tokenRedisDb;

    private <T> T execute(JedisAction<T> jedisAction) {
        Jedis jedis = null;
        try {
            jedis = getJedis(this.tokenRedisDb);
            return jedisAction.action(jedis);
        } finally {
            closeResource(jedis);
        }
    }

    public String get(final String key) {
        return execute(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public String setex(final String key, final int seconds, final String value) {
        return execute(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.setex(key, seconds, value);
            }
        });
    }

    public boolean del(final String... keys) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.del(keys) == keys.length ? true : false;
            }
        });
    }

}
