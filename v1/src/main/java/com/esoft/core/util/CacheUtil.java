package com.esoft.core.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Ehcache;

import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheUtil {
	final static Log log = LogFactory.getLog(CacheUtil.class);

	private static CacheManager manager;

	private final static String CONFIG_FILE_PATH = "ehcache.xml";
	private final static String DEFAULT_CACHE_NAME = "default_cache";

	static {
		try {
			manager = CacheManager.getInstance();
			if (manager == null)
				manager = CacheManager.create(CONFIG_FILE_PATH);
		} catch (CacheException e) {
			log.fatal("Initialize cache manager failed.", e);
		}
	}

	public static CacheManager getManager() {
		return manager;
	}

	/**
	 * 把对象放入缓存中
	 * 
	 * @param cache_name
	 * @param key
	 * @param value
	 */
	public synchronized static void put(String cacheName, Object key,
			Object value) {
		Cache cache = getCache(cacheName);
		if (cache != null) {
			try {
				cache.remove(key);
				Element elem = new Element(key, value);
				cache.put(elem);
			} catch (Exception e) {
				log.error(
						"put cache(" + cacheName + ") of " + key + " failed.",
						e);
			}
		}
	}

	/**
	 * 缓存对象放入默认的缓存中 DEFAULT_CACHE_NAME = "default_cache";
	 * 
	 * @param key
	 * @param value
	 */
	public synchronized static void put(Object key, Object value) {
		put(null, key, value);
	}

	/**
	 * 获取指定名称的缓存
	 * 
	 * @param arg0
	 * @return
	 * @throws IllegalStateException
	 */
	public static Cache getCache(String name) throws IllegalStateException {
		if (name == null) {
			name = DEFAULT_CACHE_NAME;
		}
		return manager.getCache(name);
	}

	/**
	 * 获取缓冲中的信息
	 * 
	 * @param cache
	 * @param key
	 * @return
	 * @throws IllegalStateException
	 * @throws CacheException
	 */
	public static Element getElement(String cache, Object key) {
		Cache cCache = getCache(cache);
		return cCache.get(key);
	}

	public static Element getElement(Object key) throws IllegalStateException,
			CacheException {
		return getElement(null, key);
	}

	public static Object getValue(String cache, Object key) {
		Element e = getElement(cache, key);
		if (e == null) {
			return null;
		}
		return getElement(cache, key).getObjectValue();
	}

	/**
	 * 从默认的缓存配置中读取缓存的值 DEFAULT_CACHE_NAME = "default_cache";
	 * 
	 * @param key
	 * @return
	 */
	public static Object getValue(Object key) {
		return getValue(null, key);
	}

	public static void remove(Object key) {
		remove(null, key);
	}

	public static void remove(String cacheName, Object key) {
		getCache(cacheName).remove(key);
	}

	/**
	 * 停止缓存管理器
	 */
	public static void shutdown() {
		if (manager != null)
			manager.shutdown();
	}

	public static void main(String[] args) {
		CacheUtil.put("key", "123");
		System.out.println(CacheUtil.getValue("key"));
	}

	/**
	 * 按缺省配置创建缓存
	 * 
	 * @param cacheName
	 */
	public static void createCache(String cacheName) {
		getManager().addCache(cacheName);
	}

	/**
	 * 添加缓存
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void put(String cacheName, String key, Object value) {
		Ehcache cache = getManager().getEhcache(cacheName);
		cache.put(new Element(key, value));
	}

	/**
	 * 根据缓存名与key获取值
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static Object get(String cacheName, String key) {
		Ehcache cache = getManager().getEhcache(cacheName);
		Element e = cache.get(key);
		return e == null ? null : e.getObjectValue();
	}

	/**
	 * 获取缓存名
	 * 
	 * @return
	 */
	public static String[] getCacheNames() {
		return getManager().getCacheNames();
	}

	/**
	 * 获取缓存的Keys
	 * 
	 * @param cacheName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getKeys(String cacheName) {
		Ehcache cache = getManager().getEhcache(cacheName);
		return (List<String>) cache.getKeys();
	}

	/**
	 * 清除所有
	 */
	public static void clearAll() {
		getManager().clearAll();
	}

	/**
	 * 清空指定缓存
	 * 
	 * @param cacheName
	 */
	public static void clear(String cacheName) {
		getManager().getCache(cacheName).removeAll();
	}

	/**
	 * 删除指定对象
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static boolean remove(String cacheName, String key) {
		return getManager().getCache(cacheName).remove(key);
	}

	/**
	 * 获取缓存大小
	 * 
	 * @param cacheName
	 * @return
	 */
	public static int getSize(String cacheName) {
		return getManager().getCache(cacheName).getSize();
	}

}
