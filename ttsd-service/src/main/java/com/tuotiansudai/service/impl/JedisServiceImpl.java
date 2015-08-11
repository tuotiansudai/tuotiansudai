package com.tuotiansudai.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

/**
 * Created by Administrator on 2015/8/11.
 */
@Service("jedisService")
public class JedisServiceImpl {

    static Logger log = Logger.getLogger(JedisServiceImpl.class);

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

    public interface JedisAction<T> {
        T action(Jedis jedis);
    }

    public interface JedisActionNoResult {
        void action(Jedis jedis);
    }

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

    protected void closeResource(Jedis jedis, boolean conectionBroken) {
        try {
            if (conectionBroken) {
                jedisPool.returnBrokenResource(jedis);
            } else {
                jedisPool.returnResource(jedis);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            destroyJedis(jedis);
        }
    }

    public static void destroyJedis(Jedis jedis) {
        if ((jedis != null) && jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
                jedis.disconnect();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public Boolean del(final String... keys) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.del(keys) == keys.length ? true : false;
            }
        });
    }

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

    public void append(final String key, final String value) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.append(key, value);
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

    public List lrange(final String key, final int start, final int end) {
        return execute(new JedisAction<List>() {
            @Override
            public List action(Jedis jedis) {
                return jedis.lrange(key, start, end);
            }
        });
    }

    public Long llen(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.llen(key);
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

    public Long hlen(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hlen(key);
            }
        });
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
