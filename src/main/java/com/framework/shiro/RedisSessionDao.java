package com.framework.shiro;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSessionDao extends CachingSessionDAO {

	private static Logger logger = LoggerFactory
			.getLogger(RedisSessionDao.class);
	@Autowired
	private RedisTemplate<String, Session> redisTemplate;

	private String keyPrefix = "shiro_redis_session:";

	private String getKey(String originalKey) {
		return keyPrefix + originalKey;
	}

	@PostConstruct
	public void init() {
		logger.info("注入催收的序列/反序列类");
		CollectionSerializer<Serializable> collectionSerializer = CollectionSerializer
				.getInstance();
		redisTemplate.setDefaultSerializer(collectionSerializer);
		// redisTemplate默认采用的其实是valueSerializer，就算是采用其他ops也一样，这是一个坑。
		redisTemplate.setValueSerializer(collectionSerializer);
	}

	@Override
	protected void doDelete(Session session) {
		logger.debug("删除seesion,id=[{}]", session.getId().toString());
		try {
			String key = getKey(session.getId().toString());
			redisTemplate.delete(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	protected void doUpdate(Session session) {
		logger.debug("更新seesion,id=[{}]", session.getId().toString());
		try {
			redisTemplate.opsForValue().set(getKey(session.getId().toString()),
					session, 30, TimeUnit.MINUTES);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		logger.debug("创建seesion,id=[{}]", session.getId().toString());
		try {
			redisTemplate.opsForValue().set(getKey(session.getId().toString()),
					session, 30, TimeUnit.MINUTES);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		logger.debug("获取seesion,id=[{}]", sessionId.toString());
		Session readSession = null;
		try {
			readSession = (Session) redisTemplate.opsForValue().get(
					getKey(sessionId.toString()));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return readSession;
	}

}
