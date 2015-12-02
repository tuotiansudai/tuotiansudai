package com.tuotiansudai.cache;

import com.tuotiansudai.client.MybatisRedisCacheWrapperClient;
import com.tuotiansudai.util.SerializeUtil;
import com.tuotiansudai.util.SpringContextUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.cache.Cache;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MybatisRedisCache implements Cache{

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static MybatisRedisCacheWrapperClient mybatisRedisCacheWrapperClient = null;

    private String id;

    private final String COMMON_CACHE_KEY = "TTSD:";
    private static final String UTF_8 = "utf-8";

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
        return COMMON_CACHE_KEY + this.id + ":*";
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
        try {
            getRedisClient().set(getKey(key).getBytes(UTF_8), SerializeUtil.serialize(value));
            getRedisClient().expire(getKey(key).getBytes(UTF_8), getRedisClient().getSecond());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getObject(Object key) {
        Object value = null;
        try {
            value = SerializeUtil.unserialize(getRedisClient().get(getKey(key).getBytes(UTF_8)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        Object value = null;
        try {
            value =  getRedisClient().expire(getKey(key).getBytes(UTF_8), 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void clear() {
        try {
            Set<byte[]> keys = getRedisClient().keys(getKeys().getBytes(UTF_8));
            for (byte[] key : keys) {
                getRedisClient().expire(key, 0);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        int result = 0;
        try {
            Set<byte[]> keys = getRedisClient().keys(getKeys().getBytes(UTF_8));
            if (null != keys && !keys.isEmpty()) {
                result = keys.size();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
