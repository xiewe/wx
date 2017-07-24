package com.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.service.RedisService;
import com.framework.utils.JsonAndObjectUtils;
import com.uc.entity.BizTpl;
import com.uc.service.BizTplService;

public class BizTplServiceImpl implements BizTplService {
    private final static Logger logger = LoggerFactory.getLogger(BizTplServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(BizTpl o) {
        return redisService.ZADD(BIZTPL_KEY, o.getBizTplId(), JsonAndObjectUtils.getJson(o, BizTpl.class));
    }

    @Override
    public Long delete(BizTpl o) {
        return redisService.ZREM(BIZTPL_KEY, JsonAndObjectUtils.getJson(o, BizTpl.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(BIZTPL_KEY, 0, -1);
    }

    @Override
    public Boolean update(BizTpl o) {
        delete(o);
        return add(o);
    }

    @Override
    public List<BizTpl> findByPage(int pageSize, int pageNum) {
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
        Set<String> set = redisService.ZRANGE(BIZTPL_KEY, start, end);
        List<BizTpl> list = new ArrayList<BizTpl>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, BizTpl.class));
        }

        return list;
    }

    @Override
    public List<BizTpl> findAll() {
        Set<String> set = redisService.ZRANGE(BIZTPL_KEY, 0, -1);
        List<BizTpl> list = new ArrayList<BizTpl>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, BizTpl.class));
        }

        return list;
    }

    @Override
    public BizTpl findOne(int id) {
        Set<String> set = redisService.ZRANGEBYSCORE(BIZTPL_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, BizTpl.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(BIZTPL_KEY);
    }

}
