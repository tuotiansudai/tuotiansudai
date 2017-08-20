package com.tuotiansudai.cache;

import com.tuotiansudai.util.SerializeUtil;
import org.apache.ibatis.cache.Cache;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

public class MybatisRedisCache implements Cache {

    private final static Logger logger = Logger.getLogger(MybatisRedisCache.class);

    private final static String MYBATIS_CACHE_KEY = "mybatis:{0}:{1}";

    private final MybatisRedisCacheProvider redisProvider;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final String id;

    public MybatisRedisCache(String id) {
        this.id = id;
        this.redisProvider = MybatisRedisCacheProvider.getInstance();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        execute(jedis -> {
            jedis.setex(getKey(key).getBytes(StandardCharsets.UTF_8), redisProvider.getExpireSeconds(), SerializeUtil.serialize(value));
            logger.debug(MessageFormat.format("[Mybatis Cache] put cache key: {0}, value: {1}",
                    key, value));
        });
    }

    @Override
    public Object getObject(Object key) {
        Object o = executeGet(jedis -> SerializeUtil.deserialize(jedis.get(getKey(key).getBytes(StandardCharsets.UTF_8))));
        if (o == null) {
            logger.debug(MessageFormat.format("[Mybatis Cache] no cache key: {0}", key));
        }
        return o;
    }

    @Override
    public Object removeObject(Object key) {
        return executeGet(jedis -> jedis.expire(getKey(key).getBytes(StandardCharsets.UTF_8), 0));
    }

    @Override
    public void clear() {
        logger.info(MessageFormat.format("[Mybatis Cache] clean mybaits cache keys: {0}", getKeys()));
        execute(jedis -> {
            Set<byte[]> keys = jedis.keys(getKeys().getBytes(StandardCharsets.UTF_8));
            for (byte[] key : keys) {
                jedis.del(key);
                logger.debug(MessageFormat.format("[Mybatis Cache] clean mybaits cache key: {0}",
                        new String(key, StandardCharsets.UTF_8)));
            }
        });
    }

    @Override
    public int getSize() {
        return executeGet(jedis -> {
            int result = 0;
            Set<byte[]> keys = jedis.keys(getKeys().getBytes(StandardCharsets.UTF_8));
            if (null != keys && !keys.isEmpty()) {
                result = keys.size();
            }
            return result;
        });
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    private String getKeys() {
        return MessageFormat.format(MYBATIS_CACHE_KEY, this.id, "*");
    }

    private String getKey(Object key) {
        return MessageFormat.format(MYBATIS_CACHE_KEY, this.id, md5Hash(String.valueOf(key)));
    }

    private String md5Hash(String sourceStr) {
        StringBuilder resultStr = new StringBuilder();
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] temp = sourceStr.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(temp);
            byte[] b = md5.digest();
            for (byte aB : b) {
                char[] ob = new char[2];
                ob[0] = digit[(aB >>> 4) & 0X0F];
                ob[1] = digit[aB & 0X0F];
                resultStr.append(new String(ob));
            }
            return resultStr.toString();
        } catch (NoSuchAlgorithmException ignored) {
            return null;
        }
    }

    private void execute(Consumer<Jedis> consumer) {
        executeGet(jedis -> {
            consumer.accept(jedis);
            return null;
        });
    }

    private <T> T executeGet(Function<Jedis, T> consumer) {
        Jedis jedis = redisProvider.getResource();
        try {
            return consumer.apply(jedis);
        } catch (Exception e) {
            logger.error("cache operation failed", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
