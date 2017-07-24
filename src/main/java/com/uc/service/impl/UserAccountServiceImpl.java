package com.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.service.RedisService;
import com.framework.utils.JsonAndObjectUtils;
import com.uc.entity.APNGroupTpl;
import com.uc.entity.UserAccount;
import com.uc.service.UserAccountService;

public class UserAccountServiceImpl implements UserAccountService {
    private final static Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(UserAccount o) {
        return redisService.ZADD(USERACCOUNT_KEY, Double.valueOf(o.getSubNo()),
                JsonAndObjectUtils.getJson(o, UserAccount.class));
    }

    @Override
    public Long delete(UserAccount o) {
        return redisService.ZREM(USERACCOUNT_KEY, JsonAndObjectUtils.getJson(o, UserAccount.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(USERACCOUNT_KEY, 0, -1);
    }

    @Override
    public Boolean update(UserAccount o) {
        delete(o);
        return add(o);
    }

    @Override
    public List<UserAccount> findByPage(int pageSize, int pageNum) {
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
        Set<String> set = redisService.ZRANGE(USERACCOUNT_KEY, start, end);
        List<UserAccount> list = new ArrayList<UserAccount>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, UserAccount.class));
        }

        return list;
    }

    @Override
    public List<UserAccount> findAll() {
        Set<String> set = redisService.ZRANGE(USERACCOUNT_KEY, 0, -1);
        List<UserAccount> list = new ArrayList<UserAccount>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, UserAccount.class));
        }

        return list;
    }

    @Override
    public UserAccount findOne(double id) {
        Set<String> set = redisService.ZRANGEBYSCORE(USERACCOUNT_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, UserAccount.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(USERACCOUNT_KEY);
    }

    @Override
    public Long delete(double id) {
        UserAccount o = findOne(id);
        if (o != null) {
            return delete(o);
        }
        return 0L;

    }

}
