package com.tuotiansudai.cache;

import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.ibatis.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class MybatisRedisCache implements Cache{

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private String id;

    public MybatisRedisCache(final String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        redisWrapperClient.set(SerializeUtil.serialize(key.toString()),SerializeUtil.serialize(value.toString()));
    }

    @Override
    public Object getObject(Object key) {
        Object value = SerializeUtil.unserialize(redisWrapperClient.get(SerializeUtil.serialize(key.toString())));
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        return redisWrapperClient.expire(SerializeUtil.serialize(key.toString()),0);
    }

    @Override
    public void clear() {
        redisWrapperClient.flushDB();
    }

    @Override
    public int getSize() {
        return Integer.valueOf(redisWrapperClient.dbSize().toString());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
