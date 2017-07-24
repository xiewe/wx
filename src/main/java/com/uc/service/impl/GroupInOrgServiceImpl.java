package com.uc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.service.RedisService;
import com.framework.utils.JsonAndObjectUtils;
import com.uc.entity.GroupInOrg;
import com.uc.service.GroupInOrgService;

public class GroupInOrgServiceImpl implements GroupInOrgService {
    private final static Logger logger = LoggerFactory.getLogger(GroupInOrgServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Boolean add(GroupInOrg o) {
        return redisService.ZADD(GROUPINORG_KEY, o.getCreateTime() != null ? o.getCreateTime().getTime() : 1,
                JsonAndObjectUtils.getJson(o, GroupInOrg.class));
    }

    @Override
    public Long delete(GroupInOrg o) {
        return redisService.ZREM(GROUPINORG_KEY, JsonAndObjectUtils.getJson(o, GroupInOrg.class));
    }

    @Override
    public Long deleteAll() {
        return redisService.ZREMRANGEBYRANK(GROUPINORG_KEY, 0, -1);
    }

    @Override
    public Boolean update(GroupInOrg o) {
        delete(o);
        return add(o);
    }

    @Override
    public List<GroupInOrg> findByPage(int pageSize, int pageNum) {
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
        Set<String> set = redisService.ZRANGE(GROUPINORG_KEY, start, end);
        List<GroupInOrg> list = new ArrayList<GroupInOrg>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, GroupInOrg.class));
        }

        return list;
    }

    @Override
    public List<GroupInOrg> findAll() {
        Set<String> set = redisService.ZRANGE(GROUPINORG_KEY, 0, -1);
        List<GroupInOrg> list = new ArrayList<GroupInOrg>();
        for (String s : set) {
            list.add(JsonAndObjectUtils.getObject(s, GroupInOrg.class));
        }

        return list;
    }

    @Override
    public GroupInOrg findOne(double id) {
        Set<String> set = redisService.ZRANGEBYSCORE(GROUPINORG_KEY, id, id);
        if (set.size() > 0) {
            for (String s : set) {
                return JsonAndObjectUtils.getObject(s, GroupInOrg.class);
            }
        }

        return null;
    }

    @Override
    public Long findCount() {
        return redisService.ZCARD(GROUPINORG_KEY);
    }

    @Override
    public Long delete(double id) {
        GroupInOrg o = findOne(id);
        if (o != null) {
            return delete(o);
        }
        return 0L;
    }

}
