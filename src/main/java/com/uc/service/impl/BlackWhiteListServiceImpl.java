package com.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.service.RedisService;
import com.framework.utils.JsonAndObjectUtils;
import com.uc.entity.BlackWhiteList;
import com.uc.service.BlackWhiteListService;

public class BlackWhiteListServiceImpl implements BlackWhiteListService {
    private final static Logger logger = LoggerFactory.getLogger(BlackWhiteListServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(BlackWhiteList o) {
        return redisService.ZADD(BWLIST_KEY, o.getCreateTime() != null ? o.getCreateTime().getTime() : 1,
                JsonAndObjectUtils.getJson(o, BlackWhiteList.class));
    }

    @Override
    public Long delete(BlackWhiteList o) {
        return redisService.ZREM(BWLIST_KEY, JsonAndObjectUtils.getJson(o, BlackWhiteList.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(BWLIST_KEY, 0, -1);
    }

    @Override
    public Boolean update(BlackWhiteList o) {
        delete(o);
        return add(o);
    }

    @Override
    public List<BlackWhiteList> findByPage(int pageSize, int pageNum) {
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
        Set<String> set = redisService.ZRANGE(BWLIST_KEY, start, end);
        List<BlackWhiteList> list = new ArrayList<BlackWhiteList>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, BlackWhiteList.class));
        }

        return list;
    }

    @Override
    public List<BlackWhiteList> findAll() {
        Set<String> set = redisService.ZRANGE(BWLIST_KEY, 0, -1);
        List<BlackWhiteList> list = new ArrayList<BlackWhiteList>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, BlackWhiteList.class));
        }

        return list;
    }

    @Override
    public BlackWhiteList findOne(double id) {
        Set<String> set = redisService.ZRANGEBYSCORE(BWLIST_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, BlackWhiteList.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(BWLIST_KEY);
    }

    @Override
    public Long delete(double id) {
        BlackWhiteList o = findOne(id);
        if (o != null) {
            return delete(o);
        }
        return 0L;
    }

}
