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
import com.uc.entity.PhoneNoFInfo;
import com.uc.service.PhoneNoFService;

public class PhoneNoFServiceImpl implements PhoneNoFService {
    private final static Logger logger = LoggerFactory.getLogger(PhoneNoFServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(PhoneNoFInfo o) {
        return redisService.ZADD(PHONENOF_KEY, o.getCreateTime() != null ? o.getCreateTime().getTime() : 1,
                JsonAndObjectUtils.getJson(o, PhoneNoFInfo.class));
    }

    @Override
    public Long delete(PhoneNoFInfo o) {
        return redisService.ZREM(PHONENOF_KEY, JsonAndObjectUtils.getJson(o, PhoneNoFInfo.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(PHONENOF_KEY, 0, -1);
    }

    @Override
    public Boolean update(PhoneNoFInfo o) {
        delete(o);
        return add(o);
    }

    @Override
    public List<PhoneNoFInfo> findByPage(int pageSize, int pageNum) {
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
        Set<String> set = redisService.ZRANGE(PHONENOF_KEY, start, end);
        List<PhoneNoFInfo> list = new ArrayList<PhoneNoFInfo>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, PhoneNoFInfo.class));
        }

        return list;
    }

    @Override
    public List<PhoneNoFInfo> findAll() {
        Set<String> set = redisService.ZRANGE(PHONENOF_KEY, 0, -1);
        List<PhoneNoFInfo> list = new ArrayList<PhoneNoFInfo>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, PhoneNoFInfo.class));
        }

        return list;
    }

    @Override
    public PhoneNoFInfo findOne(double id) {
        Set<String> set = redisService.ZRANGEBYSCORE(PHONENOF_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, PhoneNoFInfo.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(PHONENOF_KEY);
    }

    @Override
    public Long delete(double id) {
        PhoneNoFInfo o = findOne(id);
        if (o != null) {
            return delete(o);
        }
        return 0L;

    }

}
