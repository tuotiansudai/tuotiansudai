package com.tuotiansudai.cache;

import com.tuotiansudai.client.MybatisRedisCacheWrapperClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class MybatisRedisCache implements ApplicationContextAware, Cache {

    private final static Logger logger = Logger.getLogger(MybatisRedisCache.class);

    private static MybatisRedisCacheWrapperClient mybatisRedisCacheWrapperClient;

    private final static String MYBATIS_CACHE_KEY = "mybatis:{0}:{1}";

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private String id;

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
        mybatisRedisCacheWrapperClient.set(getKey(key).getBytes(StandardCharsets.UTF_8), serialize(value));
        mybatisRedisCacheWrapperClient.expire(getKey(key).getBytes(StandardCharsets.UTF_8), mybatisRedisCacheWrapperClient.getSecond());
    }

    @Override
    public Object getObject(Object key) {
        return deserialize(mybatisRedisCacheWrapperClient.get(getKey(key).getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public Object removeObject(Object key) {
        return mybatisRedisCacheWrapperClient.expire(getKey(key).getBytes(StandardCharsets.UTF_8), 0);
    }

    @Override
    public void clear() {
        Set<byte[]> keys = mybatisRedisCacheWrapperClient.keys(getKeys().getBytes(StandardCharsets.UTF_8));
        for (byte[] key : keys) {
            mybatisRedisCacheWrapperClient.expire(key, 0);
        }
    }

    @Override
    public int getSize() {
        int result = 0;
        Set<byte[]> keys = mybatisRedisCacheWrapperClient.keys(getKeys().getBytes(StandardCharsets.UTF_8));
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
        return MessageFormat.format(MYBATIS_CACHE_KEY, this.id, "*");
    }

    private String getKey(Object key) {
        return MessageFormat.format(MYBATIS_CACHE_KEY, this.id, DigestUtils.md5Hex(String.valueOf(key)));
    }

    private byte[] serialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            //序列化
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                }
            }
            try {
                bos.close();
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }

    private Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        ObjectInputStream ois = null;
        try {
            //反序列化
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        mybatisRedisCacheWrapperClient = applicationContext.getBean(MybatisRedisCacheWrapperClient.class);
    }
}
