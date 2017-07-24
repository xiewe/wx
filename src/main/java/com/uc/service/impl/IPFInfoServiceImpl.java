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
import com.uc.entity.IPFInfo;
import com.uc.service.IPFInfoService;

@Service
public class IPFInfoServiceImpl implements IPFInfoService {
    private final static Logger logger = LoggerFactory.getLogger(IPFInfoServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(IPFInfo o) {
        return redisService.ZADD(OPTPL_KEY, o.getCreateTime() != null ? o.getCreateTime().getTime() : 1,
                JsonAndObjectUtils.getJson(o, IPFInfo.class));
    }

    @Override
    public Long delete(IPFInfo o) {
        return redisService.ZREM(OPTPL_KEY, JsonAndObjectUtils.getJson(o, IPFInfo.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(OPTPL_KEY, 0, -1);
    }

    @Override
    public Boolean update(IPFInfo o) {
        delete(o.getCreateTime() != null ? o.getCreateTime().getTime() : 1);
        return add(o);
    }

    @Override
    public List<IPFInfo> findByPage(int pageSize, int pageNum) {
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
        List<IPFInfo> list = new ArrayList<IPFInfo>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, IPFInfo.class));
        }

        return list;
    }

    @Override
    public List<IPFInfo> findAll() {
        Set<String> set = redisService.ZRANGE(OPTPL_KEY, 0, -1);
        List<IPFInfo> list = new ArrayList<IPFInfo>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, IPFInfo.class));
        }

        return list;
    }

    @Override
    public IPFInfo findOne(double id) {
        Set<String> set = redisService.ZRANGEBYSCORE(OPTPL_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, IPFInfo.class);
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
        IPFInfo o = findOne(id);
        if (o != null) {
            return delete(o);
        }
        return 0L;
    }

}
