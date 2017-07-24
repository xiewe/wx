package com.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.service.RedisService;
import com.framework.utils.JsonAndObjectUtils;
import com.uc.entity.Organization;
import com.uc.service.OrganizationService;

public class OrganizationServiceImpl implements OrganizationService {
    private final static Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(Organization o) {
        return redisService.ZADD(ORGANIZATION_KEY, o.getOrgId(), JsonAndObjectUtils.getJson(o, Organization.class));
    }

    @Override
    public Long delete(Organization o) {
        return redisService.ZREM(ORGANIZATION_KEY, JsonAndObjectUtils.getJson(o, Organization.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(ORGANIZATION_KEY, 0, -1);
    }

    @Override
    public Boolean update(Organization o) {
        delete(o);
        return add(o);
    }

    @Override
    public List<Organization> findByPage(int pageSize, int pageNum) {
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
        Set<String> set = redisService.ZRANGE(ORGANIZATION_KEY, start, end);
        List<Organization> list = new ArrayList<Organization>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, Organization.class));
        }

        return list;
    }

    @Override
    public List<Organization> findAll() {
        Set<String> set = redisService.ZRANGE(ORGANIZATION_KEY, 0, -1);
        List<Organization> list = new ArrayList<Organization>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, Organization.class));
        }

        return list;
    }

    @Override
    public Organization findOne(int id) {
        Set<String> set = redisService.ZRANGEBYSCORE(ORGANIZATION_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, Organization.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(ORGANIZATION_KEY);
    }

}
