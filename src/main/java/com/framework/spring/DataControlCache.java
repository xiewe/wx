package com.framework.spring;

import java.util.List;
import java.util.Map;

import com.framework.entity.DataControl;

/**
 * 缓存用户数据权限信息
 * 
 */
public class DataControlCache {

	// 用户拥有的数据权限控制 key:username;
	// value:Map<String,List<DataControl>>>key:permission.sn
	private static java.util.Hashtable<String, Map<String, List<DataControl>>> cacheMap = new java.util.Hashtable<String, Map<String, List<DataControl>>>();

	// 添加cache
	public synchronized static void add(String key,
			Map<String, List<DataControl>> value) {
		cacheMap.put(key, value);
	}

	// 获取cache
	public synchronized static Map<String, List<DataControl>> get(String key) {
		return cacheMap.get(key);
	}

	// 移除cache
	public synchronized static void remove(String key) {
		if (cacheMap.get(key) != null)
			cacheMap.remove(key);
	}

	// 清理所有cache对象
	public synchronized static void clear() {
		cacheMap.clear();
	}
}
