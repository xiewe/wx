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
import com.uc.entity.OPTpl;
import com.uc.service.OPTplService;

@Service
public class OPTplServiceImpl implements OPTplService {
    private final static Logger logger = LoggerFactory.getLogger(OPTplServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(OPTpl o) {
        return redisService.ZADD(OPTPL_KEY, o.getOpId(), JsonAndObjectUtils.getJson(o, OPTpl.class));
    }

    @Override
    public Long delete(OPTpl o) {
        return redisService.ZREM(OPTPL_KEY, JsonAndObjectUtils.getJson(o, OPTpl.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(OPTPL_KEY, 0, -1);
    }

    @Override
    public Boolean update(OPTpl o) {
        delete(o.getOpId());
        return add(o);
    }

    @Override
    public List<OPTpl> findByPage(int pageSize, int pageNum) {
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
        List<OPTpl> list = new ArrayList<OPTpl>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, OPTpl.class));
        }

        return list;
    }

    @Override
    public List<OPTpl> findAll() {
        Set<String> set = redisService.ZRANGE(OPTPL_KEY, 0, -1);
        List<OPTpl> list = new ArrayList<OPTpl>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, OPTpl.class));
        }

        return list;
    }

    @Override
    public OPTpl findOne(double id) {
        Set<String> set = redisService.ZRANGEBYSCORE(OPTPL_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, OPTpl.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(OPTPL_KEY);
    }

    @Override
    public Long delete(double id) {
        OPTpl o = findOne(id);
        if (o != null) {
            return delete(o);
        }
        return 0L;

    }

}
