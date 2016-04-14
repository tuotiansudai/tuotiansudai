package com.tuotiansudai.client;

import com.tuotiansudai.util.SerializeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RedisWrapperClient extends AbstractRedisWrapperClient {

    static Logger logger = Logger.getLogger(RedisWrapperClient.class);

    @Value("${common.redis.db}")
    private int redisDb;

    @Value("${common.mybatis.cache.db}")
    private int mybatisDb;

    private <T> T execute(JedisAction<T> jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedisPool().getResource();
            if (StringUtils.isNotEmpty(getRedisPassword())) {
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
            jedis = getJedisPool().getResource();
            if (StringUtils.isNotEmpty(getRedisPassword())) {
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

    public String clearMybatisCache() {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedisPool().getResource();
            if (StringUtils.isNotEmpty(getRedisPassword())) {
                jedis.auth(getRedisPassword());
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

    public void setex(final String key, final int seconds, final String value) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.setex(key, seconds, value);
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

    public boolean setnx(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.setnx(key, value) == 1;
            }
        });
    }

    public boolean delPattern(final String pattern) {
        Set<String> keys = execute(new JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.keys(pattern);
            }
        });
        return keys.size() == 0 ? true : this.del(keys.toArray(new String[]{}));
    }

    public boolean del(final String... keys) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.del(keys) == keys.length ? true : false;
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

    public Long rpush(final String key, final String... values) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.rpush(key, values);
            }
        });
    }

    public List<String> lrange(final String key, final int start, final int end) {
        return execute(new JedisAction<List<String>>() {
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

    public Long sadd(final String key, final String... values) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.sadd(key, values);
            }
        });
    }

    public Long scard(final String key, final String... values) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.scard(key);
            }
        });
    }

    public Set smembers(final String key) {
        return execute(new JedisAction<Set>() {
            @Override
            public Set action(Jedis jedis) {
                return jedis.smembers(key);
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

    public void hset(final String key, final String hkey, final String value, final int lifeSecond) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hset(key, hkey, value);
                jedis.expire(key, lifeSecond);
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

    public Long incr(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public Long incr(final String key, final long increasement) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.incrBy(key, increasement);
            }
        });
    }

    public Long decr(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    public boolean hexists(final String key, final String field) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.hexists(key, field);
            }
        });
    }

    public Long zadd(final String key, final long score, final String member) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.zadd(key, score, member);
            }
        });
    }

    public Long zcount(final String key, final long min, final long max) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.zcount(key, min, max);
            }
        });
    }

    public Double zincrby(final String key, final long score, final String member) {
        return execute(new JedisAction<Double>() {
            @Override
            public Double action(Jedis jedis) {
                return jedis.zincrby(key, score, member);
            }
        });
    }

    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long stop) {
        return execute(new JedisAction<Set<Tuple>>() {
            @Override
            public Set<Tuple> action(Jedis jedis) {
                return jedis.zrevrangeWithScores(key, start, stop);
            }
        });
    }

    public Long zrevrank(final String key, final String member) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.zrevrank(key, member);
            }
        });
    }

    public Double zscore(final String key, final String member) {
        return execute(new JedisAction<Double>() {
            @Override
            public Double action(Jedis jedis) {
                return jedis.zscore(key, member);
            }
        });
    }


    public void setSeri(final String key, final Object value) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.set(key.getBytes(), SerializeUtil.serialize(value));
            }
        });
    }

    public Object getSeri(final String key) {
        return execute(new JedisAction() {
            @Override
            public Object action(Jedis jedis) {
                return SerializeUtil.deserialize(jedis.get(key.getBytes()));
            }
        });
    }

    public void hsetSeri(final String key, final String field, final Object value) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hset(key.getBytes(), field.getBytes(), SerializeUtil.serialize(value));
            }
        });
    }

    public Boolean hexistsSeri(final String key, final String field) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.hexists(key.getBytes(), field.getBytes());
            }
        });
    }

    public Object hgetSeri(final String key, final String field) {
        return execute(new JedisAction() {
            @Override
            public Object action(Jedis jedis) {
                return SerializeUtil.deserialize(jedis.hget(key.getBytes(), field.getBytes()));
            }
        });
    }

    public Map<byte[], byte[]> hgetAllSeri(final String key) {
        return execute(new JedisAction<Map<byte[], byte[]>>() {
            @Override
            public Map<byte[], byte[]> action(Jedis jedis) {
                return jedis.hgetAll(key.getBytes());
            }
        });
    }

    public Map<String, String> hgetAll(final String key) {
        return execute(new JedisAction<Map<String, String>>() {
            @Override
            public Map<String, String> action(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

    public List<byte[]> hgetValuesSeri(final String key) {
        return execute(new JedisAction<List<byte[]>>() {
            @Override
            public List<byte[]> action(Jedis jedis) {
                return jedis.hvals(key.getBytes());
            }
        });
    }

    public Long hdelSeri(final String key, final String field) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hdel(key.getBytes(), field.getBytes());
            }
        });
    }
}
