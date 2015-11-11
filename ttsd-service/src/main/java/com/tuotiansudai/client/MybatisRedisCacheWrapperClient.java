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

@Component
public class MybatisRedisCacheWrapperClient extends RedisWrapperClient{

    static Logger logger = Logger.getLogger(MybatisRedisCacheWrapperClient.class);

    @Value("${redis.cache.db}")
    private int redisDb;

    @Value("${redis.cache.second}")
    private int second;

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

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
            pool = new JedisPool(config, getRedisHost(), getRedisPort());
        }
        return pool;
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
            if(StringUtils.isNotEmpty(getRedisPassword())){
                jedis.auth(getRedisPassword());
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
            if(StringUtils.isNotEmpty(getRedisPassword())){
                jedis.auth(getRedisPassword());
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

    public byte[] get(final byte[] key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<byte[]>() {
            @Override
            public byte[] action(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public boolean exists(final byte[] key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    public Object expire(final byte[] key,final int seconds) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Object>() {
            @Override
            public Object action(Jedis jedis) {
                return jedis.expire(key,seconds);
            }
        });
    }

    public void flushDB() {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.flushDB();
            }
        });
    }

    public Long dbSize() {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.dbSize();
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

    public void set(final byte[] key,final byte[] value) {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.set(key, value);
            }
        });
    }

}
