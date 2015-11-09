package com.tuotiansudai.cache;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.utils.SerializeUtil;
import com.tuotiansudai.utils.SpringContextUtil;
import org.apache.ibatis.cache.Cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MybatisRedisCache implements Cache{

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static RedisWrapperClient redisWrapperClient = null;

    private String id;

    private RedisWrapperClient getRedisWrapperClient() {
        if (redisWrapperClient == null) {
            redisWrapperClient = SpringContextUtil.getBeanByType(RedisWrapperClient.class);
        }
        return redisWrapperClient;
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
        getRedisWrapperClient().set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
    }

    @Override
    public Object getObject(Object key) {
        Object value = SerializeUtil.unserialize(getRedisWrapperClient().get(SerializeUtil.serialize(key.toString())));
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        return getRedisWrapperClient().expire(SerializeUtil.serialize(key.toString()), 0);
    }

    @Override
    public void clear() {
        getRedisWrapperClient().flushDB();
    }

    @Override
    public int getSize() {
        return Integer.valueOf(getRedisWrapperClient().dbSize().toString());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
