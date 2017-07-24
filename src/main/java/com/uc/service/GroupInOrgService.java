package com.uc.service;

import java.util.List;

import com.uc.entity.GroupInOrg;

public interface GroupInOrgService {

    public static final String GROUPINORG_KEY = "groupinorg";

    public Boolean add(GroupInOrg o);

    public Long delete(double id);

    public Long delete(GroupInOrg o);

    public Long deleteAll();

    public Boolean update(GroupInOrg o);

    public List<GroupInOrg> findByPage(int pageSize, int pageNum);

    public List<GroupInOrg> findAll();

    public GroupInOrg findOne(double id);

    public Long findCount();
}
