package com.framework.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 实现对Java配置文件Properties的读取、写入与更新操作
 */
public class PropertiesUtils {
	/**
	 * 采用静态方法
	 */
	private Properties props;
	private String profile;

	public PropertiesUtils(String profile) {
		try {
			props = new Properties();
			this.profile = profile;
			props.load(this.getClass().getClassLoader()
					.getResourceAsStream(profile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取属性文件中相应键的值
	 *
	 * @param key
	 *            主键
	 * @return String
	 */
	public String getKeyValue(String key) {
		return props.getProperty(key);
	}

	/**
	 * 根据主键key读取主键的值value
	 *
	 * @param filePath
	 *            属性文件路径
	 * @param key
	 *            键名
	 */
	public String readValue(String filePath, String key) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			props.load(in);
			return props.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 更新（或插入）一对properties信息(主键及其键值) 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插件一对键值。
	 *
	 * @param keyname
	 *            键名
	 * @param keyvalue
	 *            键值
	 */
	public void writeProperties(String keyname, String keyvalue) {
		try {
			// 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			OutputStream fos = new FileOutputStream(profile);
			props.setProperty(keyname, keyvalue);
			// 以适合使用 load 方法加载到 Properties 表中的格式，
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			props.store(fos, "Update '" + keyname + "' value");
		} catch (IOException e) {
			System.err.println("属性文件更新错误");
		}
	}
}
