package com.framework.service;

public interface RedisService {
	/**
	 * 为指定的KEY产生一个TOKEN设置有效期并缓存 ,TOKEN的有效期是固定的，即TOKEN产生的时间后7天内有效
	 * 
	 * @param uid
	 * @return token
	 */
	String generateToken(String uid);

	/**
	 * 验证用户TOKEN
	 * 
	 * @param uid
	 * @param token
	 * @return boolean true:valid false:invalid
	 */
	boolean checkToken(String uid, String token);
}
