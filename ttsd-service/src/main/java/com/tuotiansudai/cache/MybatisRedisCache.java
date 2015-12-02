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

public class MybatisRedisCache implements Cache{

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static MybatisRedisCacheWrapperClient mybatisRedisCacheWrapperClient = null;

    private String id;

    private final String COMMON_CACHE_KEY = "TTSD:";

    private final String KEYSTEMPLETE = "TTSD:{0}:*";

    private MybatisRedisCacheWrapperClient getRedisClient() {
        if (mybatisRedisCacheWrapperClient == null) {
            mybatisRedisCacheWrapperClient = SpringContextUtil.getBeanByType(MybatisRedisCacheWrapperClient.class);
        }
        return mybatisRedisCacheWrapperClient;
    }

    public MybatisRedisCache(){

    }

    public MybatisRedisCache(final String id) {
        this.id = id;
    }

    private String getKeys() {
        return MessageFormat.format(KEYSTEMPLETE, this.id);
    }

    private String getKey(Object key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(COMMON_CACHE_KEY);
        stringBuilder.append(this.id).append(":");
        stringBuilder.append(DigestUtils.md5Hex(String.valueOf(key)));
        return stringBuilder.toString();
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
        return SerializeUtil.unserialize(getRedisClient().get(getKey(key).getBytes(StandardCharsets.UTF_8)));
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

}
