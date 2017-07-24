package com.framework.service;

import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

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

    /**
     * 删除指定用户的TOKEN
     * 
     * @param uid
     */
    void removeToken(String uid);

    // Key（键），简单的key-value操作
    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     * 
     * @param key
     * @return
     */
    public long TTL(String key);

    /**
     * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
     */
    public Set<String> KEYS(String pattern);

    /**
     * 实现命令：DEL key，删除一个key
     * 
     * @param key
     */
    public void DEL(String key);

    // String（字符串）
    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     * 
     * @param key
     * @param value
     */
    public void SET(String key, String value);

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     * 
     * @param key
     * @param value
     * @param timeout
     *            （以秒为单位）
     */
    public void SET(String key, String value, long timeout);

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     * 
     * @param key
     * @return value
     */
    public String GET(String key);

    // Hash（哈希表）
    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     * 
     * @param key
     * @param field
     * @param value
     */
    public void HSET(String key, String field, Object value);

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     * 
     * @param key
     * @param field
     * @return
     */
    public String HGET(String key, String field);

    /**
     * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     * 
     * @param key
     * @param fields
     */
    public void HDEL(String key, Object... fields);

    /**
     * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
     * 
     * @param key
     * @return
     */
    public Map<Object, Object> HGETALL(String key);

    // List（列表）
    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     * 
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public long LPUSH(String key, String value);

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     * 
     * @param key
     * @return 列表key的头元素。
     */
    public String LPOP(String key);

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     * 
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public long RPUSH(String key, String value);

    /**
     * 实现命令：RPOP key，移除并返回列表 key的尾元素。
     * 
     * @param key
     * @return 列表key的头元素。
     */
    public String RPOP(String key);

    // Set（集合）
    /**
     * 实现命令：SADD key member，将一个 member元素加入到集合 key当中，已经存在于集合的 member元素将被忽略。
     * 
     * @param key
     * @param member
     */
    public void SADD(String key, String member);

    /**
     * 实现命令：SMEMBERS key，返回集合 key 中的所有成员。
     * 
     * @param key
     * @return
     */
    public Set<String> SMEMEBERS(String key);

    // SortedSet（有序集合）
    /**
     * 实现命令：ZADD key score member，将一个 member元素及其 score值加入到有序集 key当中。
     * 
     * @param key
     * @param score
     * @param member
     */
    public Boolean ZADD(String key, double score, String member);

    /**
     * 实现命令：ZRANGE key start stop，返回有序集 key中，指定区间内的成员。
     * 
     * @param key
     * @param start
     * @param stop
     * @return
     */
    public Set<String> ZRANGEBYSCORE(String key, double start, double stop);

    public Set<String> ZRANGE(String key, long start, long end);

    public Long ZCARD(String key);

    public Long ZCOUNT(String key, double start, double stop);

    public Cursor<TypedTuple<String>> ZSCAN(String key, ScanOptions options);

    public Long ZREMRANGEBYRANK(String key, long start, long end);
    
    public Long ZREMRANGEBYSCORE(String key, double min, double max);

    public Long ZREM(String key, String... members);

    public void vset(String key, String value);

    public long vdel(final String... keys);

    public Long dbSize();
}
