package com.tuotiansudai.cache;

import com.tuotiansudai.client.MybatisRedisCacheWrapperClient;
import com.tuotiansudai.util.SerializeUtil;
import com.tuotiansudai.util.SpringContextUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.cache.Cache;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MybatisRedisCache implements Cache {

    private final static String COMMON_CACHE_KEY = "mybatis:{0}:{1}";

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static MybatisRedisCacheWrapperClient mybatisRedisCacheWrapperClient;

    private String id;

    private static MybatisRedisCacheWrapperClient getRedisClient() {
        if (mybatisRedisCacheWrapperClient == null) {
            mybatisRedisCacheWrapperClient = SpringContextUtil.getBeanByType(MybatisRedisCacheWrapperClient.class);
        }
        return mybatisRedisCacheWrapperClient;
    }

    public MybatisRedisCache() {

    }

    public MybatisRedisCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        getRedisClient().set(getKey(key).getBytes(StandardCharsets.UTF_8), SerializeUtil.serialize(value));
        getRedisClient().expire(getKey(key).getBytes(StandardCharsets.UTF_8), getRedisClient().getSecond());
    }

    @Override
    public Object getObject(Object key) {
        return SerializeUtil.deserialize(getRedisClient().get(getKey(key).getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public Object removeObject(Object key) {
        return getRedisClient().expire(getKey(key).getBytes(StandardCharsets.UTF_8), 0);
    }

    @Override
    public void clear() {
        Set<byte[]> keys = getRedisClient().keys(getKeys().getBytes(StandardCharsets.UTF_8));
        for (byte[] key : keys) {
            getRedisClient().expire(key, 0);
        }
    }

    @Override
    public int getSize() {
        int result = 0;
        Set<byte[]> keys = getRedisClient().keys(getKeys().getBytes(StandardCharsets.UTF_8));
        if (null != keys && !keys.isEmpty()) {
            result = keys.size();
        }
        return result;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    private String getKeys() {
        return MessageFormat.format(COMMON_CACHE_KEY, this.id, "*");
    }

    private String getKey(Object key) {
        return MessageFormat.format(COMMON_CACHE_KEY, this.id, DigestUtils.md5Hex(String.valueOf(key)));
    }

}
