package com.ttsd.redis;



import com.esoft.core.annotations.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class JedisTemplate {
    @Logger
    static Log log;

    private JedisPool jedisPool;

    @Value("${redis.password}")
    private String redisPassword;

    @Value("${redis.db}")
    private Integer redisDb;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {

        return jedisPool;
    }

    /**
     * Callback interface for template.
     */
    public interface JedisAction<T> {
        T action(Jedis jedis);
    }

    /**
     * Callback interface for template without result.
     */
    public interface JedisActionNoResult {
        void action(Jedis jedis);
    }


    /**
     * Execute with a call back action with result.
     */
    public <T> T execute(JedisAction<T> jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            if(StringUtils.isNotEmpty(redisPassword)){
                jedis.auth(redisPassword);
            }
            jedis.select(redisDb);
            return jedisAction.action(jedis);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * Execute with a call back action without result.
     */
    public void execute(JedisActionNoResult jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            if(StringUtils.isNotEmpty(redisPassword)){
                jedis.auth(redisPassword);
            }
            jedis.select(redisDb);
            jedisAction.action(jedis);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }
    /**
     * Handle jedisException, write log and return whether the connection is broken.
     */
    protected boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
           log.error(jedisException.getLocalizedMessage(), jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
                log.error(jedisException.getLocalizedMessage(), jedisException);
            } else {
                return false;
            }
        } else {
            log.error(jedisException.getLocalizedMessage(), jedisException);
        }
        return true;
    }

    /**
     * Return jedis connection to the pool, call different return methods depends on the conectionBroken status.
     */
    protected void closeResource(Jedis jedis, boolean conectionBroken) {
        try {
            if (conectionBroken) {
                jedisPool.returnBrokenResource(jedis);
            } else {
                jedisPool.returnResource(jedis);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            JedisUtils.destroyJedis(jedis);
        }

    }

    // / Common Actions ///

    /**
     * Remove the specified keys. If a given key does not exist no operation is
     * performed for this key.
     *
     * return false if one of the key is not exist.
     */
    public Boolean del(final String... keys) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                return jedis.del(keys) == keys.length ? true : false;
            }
        });
    }

    // / String Actions ///

    /**
     * Get the value of the specified key. If the key does not exist null is
     * returned. If the value stored at key is not a string an error is returned
     * because GET can only handle string values.
     */
    public String get(final String key) {
        return execute(new JedisAction<String>() {

            @Override
            public String action(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public boolean exists(final String key) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }



    /**
     * Set the string value as value of the key.
     * The string can't be longer than 1073741824 bytes (1 GB).
     */
    public void set(final String key, final String value) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(Jedis jedis) {
                jedis.set(key, value);
            }
        });
    }

    public void setex(final String key, final String value, final int seconds) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(Jedis jedis) {
                jedis.setex(key, seconds, value);
            }
        });
    }

    public Long lpush(final String key, final String... values) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.lpush(key, values);
            }
        });
    }


    public void hmset(final String key, final Map map) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hmset(key, map);
            }
        });
    }

    public void hset(final String key, final String hkey, final String value) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hset(key, hkey, value);
            }
        });
    }

    public Long hdel(final String key, final String... hkeys) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hdel(key, hkeys);
            }
        });
    }

    public int hlen(final String key) {
        return Integer.valueOf(execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hlen(key);
            }
        }).toString());
    }

    public Set hkeys(final String key) {
        return execute(new JedisAction<Set>() {
            @Override
            public Set action(Jedis jedis) {
                return jedis.hkeys(key);
            }
        });
    }

    public List hmget(final String key, final String... hkey) {
        return execute(new JedisAction<List>() {
            @Override
            public List action(Jedis jedis) {
                return jedis.hmget(key, hkey);
            }
        });
    }

    public String hget(final String key, final String hkey) {
        return execute(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.hget(key, hkey);
            }
        });
    }

}