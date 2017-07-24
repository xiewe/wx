package com.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.service.RedisService;
import com.framework.utils.JsonAndObjectUtils;
import com.uc.entity.APNTpl;
import com.uc.service.APNTplService;

public class APNTplServiceImpl implements APNTplService {
    private final static Logger logger = LoggerFactory.getLogger(APNTplServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(APNTpl o) {
        return redisService.ZADD(APNTPL_KEY, o.getApnId(), JsonAndObjectUtils.getJson(o, APNTpl.class));
    }

    @Override
    public Long delete(APNTpl o) {
        return redisService.ZREM(APNTPL_KEY, JsonAndObjectUtils.getJson(o, APNTpl.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(APNTPL_KEY, 0, -1);
    }

    @Override
    public Boolean update(APNTpl o) {
        delete(o);
        return add(o);
    }

    @Override
    public List<APNTpl> findByPage(int pageSize, int pageNum) {
        int start = 0;
        int end = 0;
        if (pageSize <= 0) {
            pageSize = 10;
        }
        if (pageNum <= 0) {
            pageNum = 1;
        }
        start = (pageNum - 1) * pageSize;
        end = start + pageSize - 1;
        Set<String> set = redisService.ZRANGE(APNTPL_KEY, start, end);
        List<APNTpl> list = new ArrayList<APNTpl>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, APNTpl.class));
        }

        return list;
    }

    @Override
    public List<APNTpl> findAll() {
        Set<String> set = redisService.ZRANGE(APNTPL_KEY, 0, -1);
        List<APNTpl> list = new ArrayList<APNTpl>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, APNTpl.class));
        }

        return list;
    }

    @Override
    public APNTpl findOne(int id) {
        Set<String> set = redisService.ZRANGEBYSCORE(APNTPL_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, APNTpl.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(APNTPL_KEY);
    }

}
