package com.tuotiansudai.cache;

import com.tuotiansudai.client.MybatisRedisCacheWrapperClient;
import com.tuotiansudai.util.SerializeUtil;
import com.tuotiansudai.util.SpringContextUtil;
import org.apache.ibatis.cache.Cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MybatisRedisCache implements Cache{

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static MybatisRedisCacheWrapperClient mybatisRedisCacheWrapperClient = null;

    private String id;

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

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        getRedisClient().set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
        getRedisClient().expire(SerializeUtil.serialize(key.toString()), getRedisClient().getSecond());
    }

    @Override
    public Object getObject(Object key) {
        Object value = SerializeUtil.unserialize(getRedisClient().get(SerializeUtil.serialize(key.toString())));
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        return getRedisClient().expire(SerializeUtil.serialize(key.toString()), 0);
    }

    @Override
    public void clear() {
        getRedisClient().flushDB();
    }

    @Override
    public int getSize() {
        return Integer.valueOf(getRedisClient().dbSize().toString());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
