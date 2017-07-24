package com.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.service.RedisService;
import com.framework.utils.JsonAndObjectUtils;
import com.uc.entity.IMSIInfo;
import com.uc.service.IMSIInfoService;

public class IMSIInfoServiceImpl implements IMSIInfoService {
    private final static Logger logger = LoggerFactory.getLogger(IMSIInfoServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(IMSIInfo o) {
        return redisService.ZADD(IMSIINFO_KEY, o.getCreateTime() != null ? o.getCreateTime().getTime() : 1,
                JsonAndObjectUtils.getJson(o, IMSIInfo.class));
    }

    @Override
    public Long delete(IMSIInfo o) {
        return redisService.ZREM(IMSIINFO_KEY, JsonAndObjectUtils.getJson(o, IMSIInfo.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(IMSIINFO_KEY, 0, -1);
    }

    @Override
    public Boolean update(IMSIInfo o) {
        delete(o);
        return add(o);
    }

    @Override
    public List<IMSIInfo> findByPage(int pageSize, int pageNum) {
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
        Set<String> set = redisService.ZRANGE(IMSIINFO_KEY, start, end);
        List<IMSIInfo> list = new ArrayList<IMSIInfo>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, IMSIInfo.class));
        }

        return list;
    }

    @Override
    public List<IMSIInfo> findAll() {
        Set<String> set = redisService.ZRANGE(IMSIINFO_KEY, 0, -1);
        List<IMSIInfo> list = new ArrayList<IMSIInfo>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, IMSIInfo.class));
        }

        return list;
    }

    @Override
    public IMSIInfo findOne(int id) {
        Set<String> set = redisService.ZRANGEBYSCORE(IMSIINFO_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, IMSIInfo.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(IMSIINFO_KEY);
    }

}
