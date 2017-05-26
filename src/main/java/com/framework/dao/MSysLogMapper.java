package com.framework.dao;

import com.framework.entity.MSysLog;

public interface MSysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MSysLog record);

    int insertSelective(MSysLog record);

    MSysLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MSysLog record);

    int updateByPrimaryKey(MSysLog record);
}