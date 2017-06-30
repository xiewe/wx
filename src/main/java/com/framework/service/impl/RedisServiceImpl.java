package com.framework.service.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.framework.AppConstants;
import com.framework.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public String generateToken(String uid) {
		String token = UUID.randomUUID().toString();
		stringRedisTemplate.boundValueOps(uid).set(token,
				AppConstants.TOKEN_EXPIRES, TimeUnit.MILLISECONDS);
		return token;
	}

	@Override
	public boolean checkToken(String uid, String token) {
		if (!stringRedisTemplate.hasKey(uid)) {
			return false;
		}
		if (!token.equals(stringRedisTemplate.boundValueOps(uid).get())) {
			return false;
		}
		return true;
	}

	@Override
	public void removeToken(String uid) {
		if (stringRedisTemplate.hasKey(uid)) {
			stringRedisTemplate.delete(uid);
		}
	}
}
