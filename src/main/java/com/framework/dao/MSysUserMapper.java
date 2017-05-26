package com.framework.dao;

import com.framework.entity.MSysUser;

public interface MSysUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MSysUser record);

    int insertSelective(MSysUser record);

    MSysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MSysUser record);

    int updateByPrimaryKey(MSysUser record);
}