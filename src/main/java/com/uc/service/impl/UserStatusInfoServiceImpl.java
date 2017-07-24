package com.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.service.RedisService;
import com.framework.utils.JsonAndObjectUtils;
import com.uc.entity.UserStatusInfo;
import com.uc.service.UserStatusInfoService;

public class UserStatusInfoServiceImpl implements UserStatusInfoService {
    private final static Logger logger = LoggerFactory.getLogger(UserStatusInfoServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(UserStatusInfo o) {
        return redisService.ZADD(OPTPL_KEY, Double.valueOf(o.getSubNo()),
                JsonAndObjectUtils.getJson(o, UserStatusInfo.class));
    }

    @Override
    public Long delete(UserStatusInfo o) {
        return redisService.ZREM(OPTPL_KEY, JsonAndObjectUtils.getJson(o, UserStatusInfo.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(OPTPL_KEY, 0, -1);
    }

    @Override
    public Boolean update(UserStatusInfo o) {
        delete(o);
        return add(o);
    }

    @Override
    public List<UserStatusInfo> findByPage(int pageSize, int pageNum) {
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
        Set<String> set = redisService.ZRANGE(OPTPL_KEY, start, end);
        List<UserStatusInfo> list = new ArrayList<UserStatusInfo>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, UserStatusInfo.class));
        }

        return list;
    }

    @Override
    public List<UserStatusInfo> findAll() {
        Set<String> set = redisService.ZRANGE(OPTPL_KEY, 0, -1);
        List<UserStatusInfo> list = new ArrayList<UserStatusInfo>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, UserStatusInfo.class));
        }

        return list;
    }

    @Override
    public UserStatusInfo findOne(int id) {
        Set<String> set = redisService.ZRANGEBYSCORE(OPTPL_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, UserStatusInfo.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(OPTPL_KEY);
    }

}
