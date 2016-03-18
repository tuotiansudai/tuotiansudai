package com.tuotiansudai.client;

import com.tuotiansudai.util.SerializeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractRedisWrapperClient {

    static Logger logger = Logger.getLogger(AbstractRedisWrapperClient.class);

    @Value("${common.redis.host}")
    private String redisHost;

    @Value("${common.redis.port}")
    private int redisPort;

    @Value("${common.redis.password}")
    private String redisPassword;

    @Value("${common.redis.db}")
    private int redisDb;

    @Value("${common.mybatis.cache.db}")
    private int mybatisDb;

    private JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    private static JedisPool pool = null;

    public JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            pool = new JedisPool(config, redisHost, redisPort);
        }
        return pool;
    }

    public String clearMybatisCache() {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            if (StringUtils.isNotEmpty(redisPassword)) {
                jedis.auth(redisPassword);
            }
            jedis.select(mybatisDb);
            return jedis.flushDB();
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }


    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public interface JedisAction<T> {
        T action(Jedis jedis);
    }

    public interface JedisActionNoResult {
        void action(Jedis jedis);
    }

    private <T> T execute(JedisAction<T> jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            if (StringUtils.isNotEmpty(redisPassword)) {
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
            if (StringUtils.isNotEmpty(redisPassword)) {
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

    public boolean setnx(final String key, final String value) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.setnx(key, value) == 1;
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

    public void append(final String key, final String value) {
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

    public Long sadd(final String key, final String... values) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.sadd(key, values);
            }
        });
    }

    public Set smembers(final String key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Set>() {
            @Override
            public Set action(Jedis jedis) {
                return jedis.smembers(key);
            }
        });
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

    public Long incr(final String key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public Long incr(final String key, final long increasement) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.incrBy(key, increasement);
            }
        });
    }

    public Long decr(final String key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    public boolean hexists(final String key, final String field) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.hexists(key, field);
            }
        });
    }


    public void setSeri(final String key, final Object value) {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.set(key.getBytes(), SerializeUtil.serialize(value));
            }
        });
    }

    public Object getSeri(final String key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction() {
            @Override
            public Object action(Jedis jedis) {
                return SerializeUtil.deserialize(jedis.get(key.getBytes()));
            }
        });
    }

    public void hsetSeri(final String key, final String field, final Object value) {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hset(key.getBytes(), field.getBytes(), SerializeUtil.serialize(value));
            }
        });
    }

    public Boolean hexistsSeri(final String key, final String field) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.hexists(key.getBytes(), field.getBytes());
            }
        });
    }

    public Object hgetSeri(final String key, final String field) {
        this.setJedisPool(getPool());
        return execute(new JedisAction() {
            @Override
            public Object action(Jedis jedis) {
                return SerializeUtil.deserialize(jedis.hget(key.getBytes(), field.getBytes()));
            }
        });
    }

    public Map<byte[], byte[]> hgetAllSeri(final String key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Map<byte[], byte[]>>() {
            @Override
            public Map<byte[], byte[]> action(Jedis jedis) {
                return jedis.hgetAll(key.getBytes());
            }
        });
    }

    public List<byte[]> hgetValuesSeri(final String key) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<List<byte[]>>() {
            @Override
            public List<byte[]> action(Jedis jedis) {
                return jedis.hvals(key.getBytes());
            }
        });
    }

    public Long hdelSeri(final String key, final String field) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hdel(key.getBytes(), field.getBytes());
            }
        });
    }
}
