package com.framework.dao;

import com.framework.entity.MSysOrganization;

public interface MSysOrganizationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MSysOrganization record);

    int insertSelective(MSysOrganization record);

    MSysOrganization selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MSysOrganization record);

    int updateByPrimaryKey(MSysOrganization record);
}