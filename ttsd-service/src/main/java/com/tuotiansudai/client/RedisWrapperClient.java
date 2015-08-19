package com.tuotiansudai.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RedisWrapperClient {

    static Logger logger = Logger.getLogger(RedisWrapperClient.class);

    @Value("${redis.ip}")
    private String redisIp;

    @Value("${redis.port}")
    private Integer redisPort;

    @Value("${redis.password}")
    private String redisPassword;

    @Value("${redis.db}")
    private Integer redisDb;

    private JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {

        return jedisPool;
    }

    private static JedisPool pool = null;

    private JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            pool = new JedisPool(config, redisIp, redisPort);
        }
        return pool;
    }

    public String getRedisIp() {
        return redisIp;
    }

    public void setRedisIp(String redisIp) {
        this.redisIp = redisIp;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    private interface JedisAction<T> {
        T action(Jedis jedis);
    }

    private interface JedisActionNoResult {
        void action(Jedis jedis);
    }

    private  <T> T execute(JedisAction<T> jedisAction) throws JedisException {
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

    private void execute(JedisActionNoResult jedisAction) throws JedisException {
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
            logger.error(jedisException.getLocalizedMessage(), jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().contains("READONLY"))) {
                logger.error(jedisException.getLocalizedMessage(), jedisException);
            } else {
                return false;
            }
        } else {
            logger.error(jedisException.getLocalizedMessage(), jedisException);
        }
        return true;
    }

    protected void closeResource(Jedis jedis, boolean connectionBroken) {
        try {
            if (connectionBroken) {
                jedisPool.returnBrokenResource(jedis);
            } else {
                jedisPool.returnResource(jedis);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            destroyJedis(jedis);
        }
    }

    private static void destroyJedis(Jedis jedis) {
        if ((jedis != null) && jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
                jedis.disconnect();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public String get(final String key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public boolean exists(final String key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    public void setex(final String key, final int seconds, final String value) {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.setex(key, seconds, value);
            }
        });
    }

    public void set(final String key, final String value) {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.set(key, value);
            }
        });
    }

    public boolean del(final String... keys) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.del(keys) == keys.length ? true : false;
            }
        });
    }

    public void append(final String key,final String value) {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.append(key, value);
            }
        });
    }

    public Long lpush(final String key, final String... values) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.lpush(key, values);
            }
        });
    }

    public List lrange(final String key, final int start, final int end) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<List>() {
            @Override
            public List action(Jedis jedis) {
                return jedis.lrange(key, start, end);
            }
        });
    }

    public int llen(final String key) {
        this.setJedisPool(getPool());
        return Integer.valueOf(execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.llen(key);
            }
        }).toString());
    }

    public void hmset(final String key, final Map map) {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hmset(key, map);
            }
        });
    }

    public void hset(final String key, final String hkey, final String value) {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hset(key, hkey, value);
            }
        });
    }

    public Long hdel(final String key, final String... hkeys) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hdel(key, hkeys);
            }
        });
    }

    public int hlen(final String key) {
        this.setJedisPool(getPool());
        return Integer.valueOf(execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hlen(key);
            }
        }).toString());
    }

    public Set hkeys(final String key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Set>() {
            @Override
            public Set action(Jedis jedis) {
                return jedis.hkeys(key);
            }
        });
    }

    public List hmget(final String key, final String... hkey) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<List>() {
            @Override
            public List action(Jedis jedis) {
                return jedis.hmget(key, hkey);
            }
        });
    }

    public String hget(final String key, final String hkey) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.hget(key, hkey);
            }
        });
    }

}
