package com.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.service.RedisService;
import com.framework.utils.JsonAndObjectUtils;
import com.uc.entity.APNGroupTpl;
import com.uc.service.APNGroupTplService;

@Service
public class APNGroupTplServiceImpl implements APNGroupTplService {
    private final static Logger logger = LoggerFactory.getLogger(APNGroupTplServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(APNGroupTpl o) {
        return redisService.ZADD(APNGRPTPL_KEY, o.getApnGroupId(), JsonAndObjectUtils.getJson(o, APNGroupTpl.class));
    }

    @Override
    public Long delete(APNGroupTpl o) {
        return redisService.ZREM(APNGRPTPL_KEY, JsonAndObjectUtils.getJson(o, APNGroupTpl.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(APNGRPTPL_KEY, 0, -1);
    }

    @Override
    public Boolean update(APNGroupTpl o) {
        delete(o.getApnGroupId());
        return add(o);
    }

    @Override
    public List<APNGroupTpl> findByPage(int pageSize, int pageNum) {
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
        Set<String> set = redisService.ZRANGE(APNGRPTPL_KEY, start, end);
        List<APNGroupTpl> list = new ArrayList<APNGroupTpl>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, APNGroupTpl.class));
        }

        return list;
    }

    @Override
    public List<APNGroupTpl> findAll() {
        Set<String> set = redisService.ZRANGE(APNGRPTPL_KEY, 0, -1);
        List<APNGroupTpl> list = new ArrayList<APNGroupTpl>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, APNGroupTpl.class));
        }

        return list;
    }

    @Override
    public APNGroupTpl findOne(double id) {
        Set<String> set = redisService.ZRANGEBYSCORE(APNGRPTPL_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, APNGroupTpl.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(APNGRPTPL_KEY);
    }

    @Override
    public Long delete(double id) {
        APNGroupTpl o = findOne(id);
        if (o != null) {
            return delete(o);
        }
        return 0L;
    }

}
