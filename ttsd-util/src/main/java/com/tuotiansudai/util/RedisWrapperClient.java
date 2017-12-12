package com.tuotiansudai.util;

import com.tuotiansudai.etcd.ETCDConfigReader;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import static com.tuotiansudai.util.SerializeUtil.serialize;

public class RedisWrapperClient extends AbstractRedisWrapperClient {

    static Logger logger = Logger.getLogger(RedisWrapperClient.class);

    private final static RedisWrapperClient redisWrapperClient = new RedisWrapperClient();

    private static int COMMON_REDIS_DB = Integer.parseInt(ETCDConfigReader.getReader().getValue("common.redis.db"));

    private RedisWrapperClient() {
    }

    public static RedisWrapperClient getInstance() {
        return redisWrapperClient;
    }

    private <T> T execute(JedisAction<T> jedisAction) {
        Jedis jedis = null;
        try {
            jedis = getJedis(COMMON_REDIS_DB);
            return jedisAction.action(jedis);
        } finally {
            closeResource(jedis);
        }
    }

    public String get(final String key) {
        return execute(jedis -> jedis.get(key));
    }

    public boolean exists(final String key) {
        return execute(jedis -> jedis.exists(key));
    }

    public void expire(final String key, final int seconds) {
        execute(jedis -> jedis.expire(key, seconds));
    }

    public String setex(final String key, final int seconds, final String value) {
        return execute(jedis -> jedis.setex(key, seconds, value));
    }

    public String set(final String key, final String value) {
        return execute(jedis -> jedis.set(key, value));
    }

    public boolean setnx(final String key, final String value) {
        return execute(jedis -> jedis.setnx(key, value) == 1);
    }

    public boolean delPattern(final String pattern) {
        Set<String> keys = execute(jedis -> jedis.keys(pattern));
        return keys.size() == 0 || this.del(keys.toArray(new String[keys.size()]));
    }

    public boolean del(final String... keys) {
        return execute(jedis -> jedis.del(keys) == keys.length);
    }

    public Long append(final String key, final String value) {
        return execute(jedis -> jedis.append(key, value));
    }

    public Long lpush(final String key, final String... values) {
        return execute(jedis -> jedis.lpush(key, values));
    }

    public String lpop(final String key) {
        return execute(jedis -> jedis.lpop(key));
    }

    public Long rpush(final String key, final String... values) {
        return execute(jedis -> jedis.rpush(key, values));
    }

    public List<String> lrange(final String key, final int start, final int end) {
        return execute(jedis -> jedis.lrange(key, start, end));
    }

    public Long llen(final String key) {
        return execute(jedis -> jedis.llen(key));
    }

    public Long sadd(final String key, final String... values) {
        return execute(jedis -> jedis.sadd(key, values));
    }

    public Long scard(final String key, final String... values) {
        return execute(jedis -> jedis.scard(key));
    }

    public Set smembers(final String key) {
        return execute((JedisAction<Set>) jedis -> jedis.smembers(key));
    }

    public String hmset(final String key, final Map map) {
        return execute(jedis -> jedis.hmset(key, map));
    }

    public String hmset(final String key, final Map map, int lifeSecond) {
        return execute(jedis -> {
            jedis.expire(key, lifeSecond);
            return jedis.hmset(key, map);
        });
    }

    public Long hset(final String key, final String hkey, final String value) {
        return execute(jedis -> jedis.hset(key, hkey, value));
    }

    public Long hset(final String key, final String hkey, final String value, final int lifeSecond) {
        return execute(jedis -> {
            jedis.expire(key, lifeSecond);
            return jedis.hset(key, hkey, value);
        });
    }

    public Long hdel(final String key, final String... hkeys) {
        return execute(jedis -> jedis.hdel(key, hkeys));
    }

    public int hlen(final String key) {
        return Integer.valueOf(execute(jedis -> jedis.hlen(key)).toString());
    }

    public Set hkeys(final String key) {
        return execute((JedisAction<Set>) jedis -> jedis.hkeys(key));
    }

    public List hmget(final String key, final String... hkey) {
        return execute((JedisAction<List>) jedis -> jedis.hmget(key, hkey));
    }

    public String hget(final String key, final String hkey) {
        return execute(jedis -> jedis.hget(key, hkey));
    }

    public Long incr(final String key) {
        return execute(jedis -> jedis.incr(key));
    }

    public Long incr(final String key, final long increasement) {
        return execute(jedis -> jedis.incrBy(key, increasement));
    }

    public Long incrEx(final String key, int seconds) {
        return execute(jedis -> {
            long val = jedis.incr(key);
            jedis.expire(key, seconds);
            return val;
        });
    }

    public Long incrEx(final String key, int seconds, final long increasement) {
        return execute(jedis -> {
            long val = jedis.incrBy(key, increasement);
            jedis.expire(key, seconds);
            return val;
        });
    }

    public Long decr(final String key) {
        return execute(jedis -> jedis.decr(key));
    }

    public Long decrEx(final String key, int seconds, final long decrement) {
        return execute(jedis -> {
            long val = jedis.decrBy(key, decrement);
            jedis.expire(key, seconds);
            return val;
        });
    }

    public boolean hexists(final String key, final String field) {
        return execute(jedis -> jedis.hexists(key, field));
    }

    public Long zadd(final String key, final long score, final String member) {
        return execute(jedis -> jedis.zadd(key, score, member));
    }

    public Long zcount(final String key, final long min, final long max) {
        return execute(jedis -> jedis.zcount(key, min, max));
    }

    public Double zincrby(final String key, final long score, final String member) {
        return execute(jedis -> jedis.zincrby(key, score, member));
    }

    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long stop) {
        return execute(jedis -> jedis.zrevrangeWithScores(key, start, stop));
    }

    public Long zrevrank(final String key, final String member) {
        return execute(jedis -> jedis.zrevrank(key, member));
    }

    public Double zscore(final String key, final String member) {
        return execute(jedis -> jedis.zscore(key, member));
    }

    public Long hsetSeri(final String key, final String field, final Object value) {
        return execute(jedis -> jedis.hset(key.getBytes(), field.getBytes(), serialize(value)));
    }

    public Boolean hexistsSeri(final String key, final String field) {
        return execute(jedis -> jedis.hexists(key.getBytes(), field.getBytes()));
    }

    @SuppressWarnings(value = "unchecked")
    public Object hgetSeri(final String key, final String field) {
        return execute(jedis -> SerializeUtil.deserialize(jedis.hget(key.getBytes(), field.getBytes())));
    }

    public Map<byte[], byte[]> hgetAllSeri(final String key) {
        return execute(jedis -> jedis.hgetAll(key.getBytes()));
    }

    public Map<String, String> hgetAll(final String key) {
        return execute(jedis -> jedis.hgetAll(key));
    }

    public List<byte[]> hgetValuesSeri(final String key) {
        return execute(jedis -> jedis.hvals(key.getBytes()));
    }

    public Long hdelSeri(final String key, final String field) {
        return execute(jedis -> jedis.hdel(key.getBytes(), field.getBytes()));
    }

    public Long hincrby(final String key, final String field, final long increasement) {
        return execute(jedis -> jedis.hincrBy(key, field, increasement));
    }

    public Long ttl(final String key) {
        return execute(jedis -> jedis.ttl(key));
    }

    public Long hsetSeri(final String key, final String field, final Object value, final int lifeSecond) {
        return execute(jedis -> {
            jedis.expire(key.getBytes(), lifeSecond);
            return jedis.hset(key.getBytes(), field.getBytes(), serialize(value));
        });
    }
}
