package com.sjn.utils;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

/**
 * Ehcache缓存操作工厂类
 * 
 * @author 董华健
 */
public class EhcacheFactoryUtils {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(EhcacheFactoryUtils.class);

	public static final String cache_name_page = "SimplePageCachingFilter";// URL缓存
	public static final String cache_name_system = "system";// 系统缓存，主要是权限和数据字典等

	private static EhcacheFactoryUtils ehcacheImpl = new EhcacheFactoryUtils();

	private static CacheManager cacheManager = CacheManager.getInstance();

	private EhcacheFactoryUtils() {

	}

	public static EhcacheFactoryUtils getInstance() {
		return ehcacheImpl;
	}

	public static CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * 获取指定cache
	 * 
	 * @param cacheName
	 * @return
	 */
	public Cache getCache(String cacheName) {
		Cache ehcache = cacheManager.getCache(cacheName);
		return ehcache;
	}

	/**
	 * 获取指定cache所有key
	 * 
	 * @param cacheName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCacheKeys(String cacheName) {
		Cache ehcache = cacheManager.getCache(cacheName);
		return ehcache.getKeys();
	}

	/**
	 * 获取指定cache值
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public Object get(String cacheName, String key) {
		Cache ehcache = cacheManager.getCache(cacheName);
		Element element = ehcache.get(key);
		if (null != element) {
			return element.getObjectValue();
		}
		return null;
	}

	/**
	 * 添加cache值
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public void add(String cacheName, String key, Object value) {
		Cache ehcache = cacheManager.getCache(cacheName);
		Element element = new Element(key, value);
		ehcache.put(element);
	}

	/**
	 * 更新cache值
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public void update(String cacheName, String key, Object value) {
		Cache ehcache = cacheManager.getCache(cacheName);
		Element element = new Element(key, value);
		ehcache.put(element);
	}

	/**
	 * 删除cache值
	 * 
	 * @param cacheName
	 * @param key
	 */
	public void delete(String cacheName, String key) {
		Cache ehcache = cacheManager.getCache(cacheName);
		ehcache.remove(key);
	}

}
