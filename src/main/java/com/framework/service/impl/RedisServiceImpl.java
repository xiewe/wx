package com.framework.service.impl;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
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
        stringRedisTemplate.boundValueOps(uid).set(token, AppConstants.TOKEN_EXPIRES, TimeUnit.MILLISECONDS);
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

    @Override
    public long TTL(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    @Override
    public Set<String> KEYS(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

    @Override
    public void DEL(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public void SET(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void SET(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public String GET(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void HSET(String key, String field, Object value) {
        stringRedisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public String HGET(String key, String field) {
        return (String) stringRedisTemplate.opsForHash().get(key, field);
    }

    @Override
    public void HDEL(String key, Object... fields) {
        stringRedisTemplate.opsForHash().delete(key, fields);
    }

    @Override
    public Map<Object, Object> HGETALL(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    @Override
    public long LPUSH(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public String LPOP(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    @Override
    public long RPUSH(String key, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public String RPOP(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    @Override
    public void SADD(String key, String member) {
        stringRedisTemplate.opsForSet().add(key, member);
    }

    @Override
    public Set<String> SMEMEBERS(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    @Override
    public void ZADD(String key, double score, String member) {
        stringRedisTemplate.opsForZSet().add(key, member, score);
    }

    @Override
    public Set<String> ZRANGE(String key, double start, double stop) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, start, stop);
    }

    @Override
    public Long ZREM(String key, String... members) {
        return stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                for (int i = 0; i < members.length; i++) {
                    connection.zRem(key.getBytes(), members[i].getBytes());
                }
                return 0L;
            }
        });
    }

    public void set(final byte[] key, final byte[] value, final long liveTime) {
        stringRedisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }

    @Override
    public void vset(String key, String value) {
        this.set(key.getBytes(), value.getBytes(), 0L);
    }

    @Override
    public long vdel(final String... keys) {
        return stringRedisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    @Override
    public Long dbSize() {
        return stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

}
