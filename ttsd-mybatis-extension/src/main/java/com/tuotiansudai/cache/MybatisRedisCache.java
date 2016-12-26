package com.tuotiansudai.cache;

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
            jedis.set(getKey(key).getBytes(StandardCharsets.UTF_8), serialize(value));
            jedis.expire(getKey(key).getBytes(StandardCharsets.UTF_8), redisProvider.getExpireSeconds());
        });
    }

    @Override
    public Object getObject(Object key) {
        return executeGet(jedis -> deserialize(jedis.get(getKey(key).getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public Object removeObject(Object key) {
        return executeGet(jedis -> jedis.expire(getKey(key).getBytes(StandardCharsets.UTF_8), 0));
    }

    @Override
    public void clear() {
        execute(jedis -> {
            Set<byte[]> keys = jedis.keys(getKeys().getBytes(StandardCharsets.UTF_8));
            for (byte[] key : keys) {
                jedis.expire(key, 0);
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

    private String md5Hash(String sourceStr) {
        String resultStr = "";
        try {
            byte[] temp = sourceStr.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(temp);
            byte[] b = md5.digest();
            for (int i = 0; i < b.length; i++) {
                char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                        '9', 'A', 'B', 'C', 'D', 'E', 'F'};
                char[] ob = new char[2];
                ob[0] = digit[(b[i] >>> 4) & 0X0F];
                ob[1] = digit[b[i] & 0X0F];
                resultStr += new String(ob);
            }
            return resultStr;
        } catch (NoSuchAlgorithmException ignored) {
            return null;
        }
    }

    private Object execute(Consumer<Jedis> consumer) {
        return executeGet(jedis -> {
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
